package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentChatBinding
import com.example.dotogether.model.Basket
import com.example.dotogether.model.Message
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.model.request.NewChatRequest
import com.example.dotogether.model.request.SendMessageRequest
import com.example.dotogether.model.response.ChatResponse
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
import com.example.dotogether.view.adapter.MessageAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.concurrent.thread
import kotlin.math.abs


@AndroidEntryPoint
class ChatFragment : BaseFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, HolderListener.RightMessageHolderListener {

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

    private lateinit var chatResponse: ChatResponse

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
                        .placeholder(R.drawable.pilgrim)
                        .error(R.drawable.pilgrim)
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

        dialogBinding.notificationSwitch.visibility = View.VISIBLE
        dialogBinding.notificationSwitch.setOnCheckedChangeListener(this)

        messageAdapter = MessageAdapter(messages, isGroup, this)
        binding.messageRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.messageRv.adapter = messageAdapter
        binding.messageRv.addOnScrollListener(scrollListener)
    }

    private fun initObserve() {
        viewModel.getMyUserFromLocale().observe(viewLifecycleOwner) {
            myUser = it
            if (!chatId.isNullOrEmpty()) {
                viewModel.getChat(chatId!!)
            } else {
                onError()
            }
        }
        viewModel.newChat.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        chatResponse = it
                        it.id?.let { id ->
                            changeNotificationSwitch()
                            chatId = id.toString()
                            getData()
                        }
                    }
                }
                is Resource.Error -> {
                    onError(resource.message)
                }
                is Resource.Loading -> {

                }
            }
        }
        viewModel.chat.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        chatResponse = it
                        it.id?.let { id ->
                            changeNotificationSwitch()
                            chatId = id.toString()
                            getData()
                        }
                    }
                }
                is Resource.Error -> {
                    onError(resource.message)
                }
                is Resource.Loading -> {

                }
            }
        }
        viewModel.updateChat.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    dialog.hide()
                    resource.data?.let {
                        chatResponse = it
                        showToast(if (it.is_mute == 1) getString(R.string.notification_closed) else getString(R.string.notification_opened))
                    }
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(resource.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
            }
        }
        setBasket()
    }

    private fun changeNotificationSwitch() {
        dialogBinding.notificationSwitch.setOnCheckedChangeListener(null)
        dialogBinding.notificationSwitch.isChecked = chatResponse.is_mute == 0
        dialogBinding.notificationSwitch.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        if (navController != null) {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
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
                    bottomSheetDialog.dismiss()
                }
                else -> {}
            }
        }
    }

    fun sendMessage(message: String) {
        binding.linearIndicator.visibility = View.VISIBLE
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
        if (!chatId.isNullOrEmpty()) {
            viewModel.resetUnreadCountChat(chatId!!)

            val newReference = firebaseDatabase.getReference("chats").child(chatId!!)
            val query: Query = newReference.orderByChild("time")

            query.addValueEventListener(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange")
                    messages.clear()
                    var penultimateTime: Long? = null
                    for ((count, ds) in snapshot.children.withIndex()) {
                        val hashMap = ds.value as HashMap<*, *>
                        val messageKey = ds.key
                        val username: String? = hashMap.get("username") as String?
                        val userId: Long? = hashMap.get("user_id") as Long?
                        val userMessage: String? = hashMap.get("user_message") as String?
                        var time: Long? = if (hashMap.get("time") is Long) hashMap.get("time") as Long? else 1675252866602L

                        if (username != null && userId != null && userMessage != null && time != null) {
                            time = abs(time)

                            val message = Message(
                                messageKey,
                                username,
                                userId,
                                Constants.DATE_FORMAT_4.format(Date(time)),
                                if (userMessage == Constants.DELETE_MESSAGE_FIREBASE_KEY ) getString(R.string.delete_firebase_message) else userMessage,
                                myUser.id == userId.toInt())

                            if (chatUser?.unread_count != 0 && chatUser?.unread_count == count) {
                                val unreadMessage = Message("messageKey", username,0, Constants.DATE_FORMAT_4.format(Date(time)), "${chatUser?.unread_count} ${getString(R.string.message)}", true)
                                unreadMessage.isUnreadCountMessage = true
                                messages.add(unreadMessage)
                            }

                            if (penultimateTime != null && !RuntimeHelper.isSameDay(penultimateTime, time)) {
                                val unreadMessage = Message("messageKey", username, 0, Constants.DATE_FORMAT_4.format(Date(time)), Constants.DATE_FORMAT_5.format(Date(penultimateTime)), false)
                                unreadMessage.isDateMessage = true
                                messages.add(unreadMessage)
                            }
                            messages.add(message)
                        }
                        penultimateTime = time
                    }

                    penultimateTime?.let {
                        val unreadMessage = Message("messageKey", "username", 0, Constants.DATE_FORMAT_4.format(Date(penultimateTime)), Constants.DATE_FORMAT_5.format(Date(penultimateTime)), false)
                        unreadMessage.isDateMessage = true
                        messages.add(unreadMessage)
                    }

                    messageAdapter.notifyDataSetChanged()
                    binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE

                    if (chatUser?.unread_count != 0) {
                        thread {
                            Thread.sleep(100)
                            val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(requireContext()) {
                                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                                    return 50f / displayMetrics.densityDpi
                                }
                            }
                            smoothScroller.targetPosition = chatUser?.unread_count!!
                            binding.messageRv.layoutManager?.startSmoothScroll(smoothScroller)
                            chatUser?.unread_count = 0
                        }
                    }
                    binding.linearIndicator.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.message)
                    onError(error.message)
                }
            })
        } else {
            onError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatId?.let { viewModel.resetUnreadCountChat(it) }
    }

    fun onError(message: String? = null) {
        message?.let {
            showToast(it)
        }
        binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
        binding.linearIndicator.visibility = View.GONE
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        chatId?.let { viewModel.muteChat(it) }
    }

    override fun deleteMessage(message: Message) {
        showAlertDialog("Mesaj Silinecek.\nEmin misin?", object : ConfirmDialogListener {
            override fun cancel() {

            }

            override fun confirm() {
                message.key?.let {
                    firebaseDatabase.getReference("chats")
                        .child(chatId!!)
                        .child(it)
                        .child( "user_message")
                        .setValue(Constants.DELETE_MESSAGE_FIREBASE_KEY)
                }
            }
        })
    }

    private fun setBasket() {
        val basket = viewModel.getCurrentBasketSync() ?: Basket()
        basket.viewType = Constants.ViewType.VIEW_CHAT_FRAGMENT.type
        basket.viewId = chatId
        viewModel.updateBasket(basket)
    }

    override fun onStop() {
        super.onStop()
        val basket = viewModel.getCurrentBasketSync()
        basket?.let {
            basket.viewType = null
            viewModel.updateBasket(basket)
        }
    }
}