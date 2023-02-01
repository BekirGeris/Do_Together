package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentChatBinding
import com.example.dotogether.model.Message
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.model.request.NewChatRequest
import com.example.dotogether.model.request.SendMessageRequest
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
import kotlin.concurrent.thread


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

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            binding.downBtn.visibility = if (recyclerView.canScrollVertically(1)) View.VISIBLE  else View.GONE
        }
    }

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
            if (isGroup) {
                RuntimeHelper.glide(
                    requireContext(),
                    RequestOptions()
                        .placeholder(R.drawable.ic_groups)
                        .error(R.drawable.ic_groups)
                ).load(it.img).into(binding.chatsUserImage)
            } else {
                RuntimeHelper.glideForPersonImage(requireContext()).load(it.img).into(binding.chatsUserImage)
            }
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
        binding.downBtn.setOnClickListener(this)

        dialogBinding.clearChat.visibility = View.VISIBLE
        dialogBinding.clearChat.setOnClickListener(this)

        messageAdapter = MessageAdapter(messages, isGroup)
        binding.messageRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.messageRv.adapter = messageAdapter
        binding.messageRv.addOnScrollListener(scrollListener)
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
                        chatUser?.id?.let { navController.navigate(ChatFragmentDirections.actionChatFragmentToTargetFragment(targetId = it)) }
                    } else {
                        chatUser?.id?.let { navController.navigate(ChatFragmentDirections.actionChatFragmentToProfileFragment(userId = it)) }
                    }
                }
                binding.moreSettingBtn -> {
                    bottomSheetDialog.tryShow()
                }
                binding.attachBtn -> {

                }
                binding.downBtn -> {
                    binding.messageRv.smoothScrollToPosition(0)
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
        binding.messageRv.smoothScrollToPosition(0)
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
            viewModel.resetUnreadCountChat(it)

            val newReference = firebaseDatabase.getReference("chats").child(it)
            val query: Query = newReference.orderByChild("time")

            query.addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange")
                    messages.clear()
                    for ((count, ds) in snapshot.children.withIndex()) {
                        val hashMap = ds.value as HashMap<*, *>
                        val username: String? = hashMap.get("username") as String?
                        val userId: Long? = hashMap.get("user_id") as Long?
                        val userMessage: String? = hashMap.get("user_message") as String?
                        val time: Long? = if (hashMap.get("time") is Long) hashMap.get("time") as Long? else 1675252866602L

                        if (username != null && userId != null && userMessage != null && time != null) {
                            if (chatUser?.unread_count != 0 && chatUser?.unread_count == snapshot.children.count() - count) {
                                val message = Message(username, Constants.DATE_FORMAT_4.format(Date(time)), "${chatUser?.unread_count} Okunmamış Mesaj", true)
                                message.isUnreadCountMessage = true
                                messages.add(message)
                            }
                            messages.add(Message(username, Constants.DATE_FORMAT_4.format(Date(time)), userMessage, myUser.id == userId.toInt()))
                        }
                    }
                    messages.reverse()
                    messageAdapter.notifyDataSetChanged()
                    binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
                    chatUser?.unread_count?.let { unread_count ->
                        thread {
                            Thread.sleep(100)
                            val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(requireContext()) {
                                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                                    return 100f / displayMetrics.densityDpi
                                }
                            }
                            smoothScroller.targetPosition = unread_count
                            binding.messageRv.layoutManager?.startSmoothScroll(smoothScroller)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast(error.message)
                    binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatId?.let { viewModel.resetUnreadCountChat(it) }
    }
}