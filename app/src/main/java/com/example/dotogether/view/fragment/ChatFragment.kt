package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentChatBinding
import com.example.dotogether.model.Message
import com.example.dotogether.model.User
import com.example.dotogether.util.Constants
import com.example.dotogether.view.adapter.MessageAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val messages = arrayListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    var isGroup = false
    private var chatId: String = ""

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var myUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)
        dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(dialogBinding.root.context, R.style.BottomSheetDialogTheme)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initViews() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        chatId = arguments?.getInt("chatId").toString()

        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.backBtn.setOnClickListener(this)
        binding.chatsUserImage.setOnClickListener(this)
        binding.chatName.setOnClickListener(this)
        binding.chatDecs.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.attachBtn.setOnClickListener(this)
        binding.sendMessageBtn.setOnClickListener(this)

        dialogBinding.clearChat.visibility = View.VISIBLE
        dialogBinding.clearChat.setOnClickListener(this)

        isGroup = arguments?.getBoolean("isGroup") ?: false
        binding.chatsUserImage.visibility = if (isGroup) View.GONE else View.VISIBLE

        messageAdapter = MessageAdapter(messages, isGroup)
        binding.messageRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.messageRv.adapter = messageAdapter
    }

    private fun initObserve() {
        viewModel.myUser.observe(viewLifecycleOwner) {
            myUser = it
            getData()
        }
        viewModel.getMyUser()
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        var action: NavDirections
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                binding.chatsUserImage, binding.chatName, binding.chatDecs -> {
                    if (isGroup) {
                        activity?.onBackPressed()
                    } else {
                        action = ChatFragmentDirections.actionChatFragmentToProfileFragment()
                        navController.navigate(action)
                    }
                }
                binding.moreSettingBtn -> {
                    bottomSheetDialog.show()
                }
                binding.attachBtn -> {

                }
                binding.sendMessageBtn -> {
                    sendMessage(binding.writeMessageEditTxt.text.toString())
                }
                dialogBinding.clearChat -> {
                    bottomSheetDialog.hide()
                }
                else -> {}
            }
        }
    }

    fun sendMessage(message: String) {
        val uuidMessage = UUID.randomUUID().toString()
        binding.writeMessageEditTxt.text.clear()

        databaseReference.child("chats").child(chatId).child(uuidMessage).child("user_message").setValue(message)
        databaseReference.child("chats").child(chatId).child(uuidMessage).child("username").setValue(myUser.username)
        databaseReference.child("chats").child(chatId).child(uuidMessage).child("user_id").setValue(myUser.id)
        databaseReference.child("chats").child(chatId).child(uuidMessage).child("time").setValue(ServerValue.TIMESTAMP)
    }

    fun getData() {
        val newReference = firebaseDatabase.getReference("chats").child(chatId)
        val query: Query = newReference.orderByChild("time")

        query.addValueEventListener(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (ds in snapshot.children) {
                    val hashMap = ds.value as HashMap<*, *>
                    val username: String? = hashMap.get("username") as String?
                    val userId: Long? = hashMap.get("user_id") as Long?
                    val userMessage: String? = hashMap.get("user_message") as String?
                    val time: Long? = hashMap.get("time") as Long?

                    if (username != null && userId != null && userMessage != null && time != null) {
                        messages.add(Message(username, Constants.DATE_FORMAT_4.format(Date(time)), userMessage, myUser.id == userId.toInt()))
                    }
                }
                messages.reverse()
                messageAdapter.notifyDataSetChanged()
                binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.message)
                binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
            }
        })
    }
}