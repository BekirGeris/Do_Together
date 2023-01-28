package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentChatBinding
import com.example.dotogether.model.Message
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SendMessageRequest
import com.example.dotogether.model.request.NewChatRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
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
    private var chatId: String? = null

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var myUser: User
    private var chatUser: OtherUser? = null

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

        isGroup = arguments?.getBoolean("isGroup") ?: false
        chatId = arguments?.getString("chatId")
        chatUser = arguments?.getParcelable("chatUser")
        Log.d(TAG, "chat id : $chatId")

        chatUser?.let {
            RuntimeHelper.glideForPersonImage(requireContext()).load(it.img).into(binding.chatsUserImage)
            binding.chatName.text = if (isGroup) it.target else it.username
        }

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
        viewModel.newChat.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.id?.let {
                        chatId = resource.data.id
                        getData()
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }

        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        if (navController != null) {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                binding.chatsUserImage, binding.chatName, binding.chatDecs -> {
                    if (isGroup) {
                        activity?.onBackPressed()
                    } else {
                        chatUser?.id?.let { navController.navigate(ChatFragmentDirections.actionChatFragmentToProfileFragment(userId = it)) }
                    }
                }
                binding.moreSettingBtn -> {
                    bottomSheetDialog.tryShow()
                }
                binding.attachBtn -> {

                }
                binding.sendMessageBtn -> {
                    if (binding.writeMessageEditTxt.text.isNotEmpty()) {
                        sendMessage(binding.writeMessageEditTxt.text.toString())
                    }
                }
                dialogBinding.clearChat -> {
                    bottomSheetDialog.hide()
                }
                else -> {}
            }
        }
    }

    fun sendMessage(message: String) {
        binding.writeMessageEditTxt.text.clear()

        if (isGroup) {
            chatId?.let { viewModel.sendMessage(SendMessageRequest(it, message)) }
        } else {
            if (chatId == null) {
                chatUser?.username?.let { viewModel.newChat(NewChatRequest(it, message)) }
            } else {
                viewModel.sendMessage(SendMessageRequest(chatId!!, message))
            }
        }
    }

    fun getData() {
        chatId?.let {
            val newReference = firebaseDatabase.getReference("chats").child(it)
            val query: Query = newReference.orderByChild("time")

            query.addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange")
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
}