package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.example.dotogether.view.adapter.holder.BaseHolder
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
class ChatFragment : BaseFragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, HolderListener.MessageHolderListener {

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
    private var replyMessage: Message? = null

    private val messageLimit = 100
    private val nextMessageSize = 20
    private var lastTime: Long? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            binding.downBtn.visibility = if (recyclerView.canScrollVertically(1)) View.VISIBLE  else View.GONE
            changeDateTxt()
        }
    }

    private val nextScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
            val lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount

            if (totalItemCount >= nextMessageSize && lastVisibleItemPosition >= totalItemCount - nextMessageSize) {
                getDataLimited()
                binding.messageRv.removeOnScrollListener(this)
            }
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
                        .placeholder(R.drawable.target_background)
                        .error(R.drawable.target_background)
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
        binding.closeReplyMessage.visibility = View.VISIBLE
        binding.closeReplyMessage.setOnClickListener(this)

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
                            onGetData(id)
                        }
                    }
                }
                is Resource.Error -> {
                    onError(resource.message + " 1")
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
                            onGetData(id)
                        }
                    }
                }
                is Resource.Error -> {
                    onError(resource.message + " 2")
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

    private fun onGetData(id: Int) {
        changeNotificationSwitch()
        chatId = id.toString()
        viewModel.resetUnreadCountChat(chatId!!)
        getDataLimited()
        observeData()
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
                    binding.messageRv.scrollToPosition(0)
                    binding.downBtn.visibility = View.GONE
                }
                binding.closeReplyMessage -> {
                    binding.replyMessageLyt.visibility = View.GONE
                    replyMessage = null
                }
                binding.sendMessageBtn -> {
                    if (binding.writeMessageEditTxt.text.isNotEmpty()) {
                        binding.replyMessageLyt.visibility = View.GONE
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

    private fun sendMessage(message: String) {
        binding.linearIndicator.visibility = View.VISIBLE
        binding.messageRv.scrollToPosition(0)
        binding.writeMessageEditTxt.text.clear()

        if (isGroup) {
            chatId?.let {
                viewModel.sendMessage(SendMessageRequest(it, message))
                sendMessageFirebase(message)
            }
        } else {
            if (chatId == null) {
                chatUser?.username?.let {
                    viewModel.newChat(NewChatRequest(it, message))
                }
            } else {
                viewModel.sendMessage(SendMessageRequest(chatId!!, message))
                sendMessageFirebase(message)
            }
        }
    }

    private fun getDataLimited() {
        if (!chatId.isNullOrEmpty()) {
            val newReference = firebaseDatabase.getReference("chats").child(chatId!!)
            val query: Query = if (lastTime != null) {
                newReference.orderByChild("time").startAfter(lastTime!!.toDouble()).limitToFirst(messageLimit)
            } else {
                newReference.orderByChild("time").limitToFirst(messageLimit)
            }

            query.addListenerForSingleValueEvent(object: ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange size: ${snapshot.childrenCount}")
                    for ((count, ds) in snapshot.children.withIndex()) {
                        val hashMap = ds.value as HashMap<*,*>
                        lastTime = if (hashMap.get("time") is Long) hashMap.get("time") as Long? else 1675252866602L
                        val message = generateMessage(ds.value as HashMap<*, *>, ds.key)
                        message?.let { messages.add(it) }
                    }
                    updateMessages()
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

    private fun observeData() {
        Log.d(TAG, "observeData")
        if (!chatId.isNullOrEmpty()) {
            val newReference = firebaseDatabase.getReference("chats").child(chatId!!)
            val query: Query = newReference.orderByChild("time").limitToFirst(messageLimit)

            query.addChildEventListener(object: ChildEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    val message = snapshot.getValue(Message::class.java)
//                    Log.d(TAG, "onChildAdded message: $message")
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Varolan bir veri değiştirildiğinde buraya girilir
                    val hashMap = snapshot.value as HashMap<*, *>
                    val message = generateMessage(hashMap, snapshot.key)
                    Log.d(TAG, "onChildChanged $message")
                    messages.map {
                        if (it.key == message?.key) {
                            it.message = message?.message
                            it.replyMessage = null
                            messageAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) { }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.message)
                    onError(error.message)
                }
            })

            val newReference2 = firebaseDatabase.getReference("chats").child(chatId!!)
            val query2: Query = newReference2.orderByChild("time").limitToFirst(1)

            query2.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for ((count, ds) in snapshot.children.withIndex()) {
                        val hashMap = ds.value as HashMap<*, *>
                        val message = generateMessage(hashMap, ds.key)

                        Log.d(TAG, "onDataChange limit 1 message: $message")
                        if (!messages.any{ it.key == message?.key }) {
                            if (message != null) {
                                messages.add(0, message)
                                updateMessages()
                            }
                        }
                    }
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

    private fun generateMessage(hashMap: HashMap<*, *>?, messageKey: String?): Message? {
        var message: Message? = null
        hashMap?.let {
            val username: String? = it.get("username") as String?
            val userId: Long? = it.get("user_id") as Long?
            val userMessage: String? = it.get("user_message") as String?
            var time: Long? = if (it.get("time") is Long) hashMap.get("time") as Long? else 1675252866602L

            if (messageKey != null && username != null && userId != null && userMessage != null && time != null) {
                time = abs(time)
                message = Message(messageKey, username, userId, time, userMessage, myUser.id == userId.toInt())
                val replyMessage = if (hashMap.get("reply_message") != null) hashMap.get("reply_message") as HashMap<*, *> else null
                message?.replyMessage = generateMessage( replyMessage, replyMessage?.get("message_key") as String?)
            }
        }
        return message
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateMessages() {
        binding.activityErrorView.visibility = if (messages.isEmpty()) View.VISIBLE else View.GONE
        binding.linearIndicator.visibility = View.GONE
        setRecyclerViewScrollListener()

        addUnreadLabel()
        goToLastReadMessage()

        messageAdapter.notifyDataSetChanged()
    }

    private fun addUnreadLabel() {
        if (chatUser?.unread_count != 0) {
            val unreadMessage = Message(
                "messageKey",
                "username",
                0,
                0,
                "${chatUser?.unread_count} ${getString(R.string.unread_message)}",
                true
            )
            unreadMessage.isUnreadCountMessage = true
            messages.add(chatUser?.unread_count!!, unreadMessage)
        }
    }

    private fun goToLastReadMessage() {
        if (chatUser?.unread_count != 0) {
            thread {
                Thread.sleep(100)
                val smoothScroller: LinearSmoothScroller =
                    object : LinearSmoothScroller(requireContext()) {
                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                            return 50f / displayMetrics.densityDpi
                        }
                    }
                smoothScroller.targetPosition = chatUser?.unread_count!! + 1
                binding.messageRv.layoutManager?.startSmoothScroll(smoothScroller)
                chatUser?.unread_count = 0
            }
        }
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
        showAlertDialog(getString(R.string.delete_message_info), object : ConfirmDialogListener {
            override fun cancel() {

            }

            override fun confirm() {
                message.key?.let {
                    firebaseDatabase.getReference("chats")
                        .child(chatId!!)
                        .child(it)
                        .child( "user_message")
                        .setValue(Constants.DELETE_MESSAGE_FIREBASE_KEY)
                    firebaseDatabase.getReference("chats")
                        .child(chatId!!)
                        .child(it)
                        .child( "reply_message")
                        .setValue(null)
                }
            }
        })
    }

    override fun copyMessage(message: Message) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", message.message)
        clipboardManager.setPrimaryClip(clipData)
        showToast(getString(R.string.message_copied))
    }

    override fun replyMessage(message: Message, isMe: Boolean) {
        replyMessage = message
        binding.replyMessageUserName.text = if (isMe) requireContext().getString(R.string.you) else message.userName
        binding.replyMessage.text = message.message
        message.message?.let {
            binding.replyMessage.text = if (it.length > 150) it.substring(0, 150) + "..." else it
        }
        binding.replyMessageLyt.visibility = View.VISIBLE
    }

    override fun goToMessageHolder(holderMessage: Message) {
        var targetIndex: Int? = null
        for ((i, m) in messages.withIndex()) {
            if (m.key == holderMessage.key) {
                targetIndex = i
                break
            }
        }

        if (targetIndex != null) {
            binding.messageRv.scrollToPosition(targetIndex)
            changeDateTxt()
            binding.messageRv.postDelayed({
                val viewHolder = binding.messageRv.findViewHolderForAdapterPosition(targetIndex) as? BaseHolder
                viewHolder?.itemView?.let { RuntimeHelper.animateBackgroundColorChange(it, R.color.dark_teal, 2000) }
            }, 200)
        } else {
            val newReference = firebaseDatabase.getReference("chats").child(chatId!!)
            val query: Query = newReference.orderByChild("time")

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for ((count, ds) in snapshot.children.withIndex()) {
                        val hashMap = ds.value as HashMap<*, *>
                        val message = generateMessage(hashMap, ds.key)

                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    goToMessageHolder(holderMessage)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.message)
                    onError(error.message)
                }
            })
        }
    }

    private fun setBasket() {
        val basket = viewModel.getCurrentBasketSync() ?: Basket()
        basket.viewType = Constants.ViewType.VIEW_CHAT_FRAGMENT.type
        basket.viewId = chatId
        viewModel.updateBasket(basket)
    }

    override fun onStop() {
        super.onStop()
        chatId?.let { viewModel.resetUnreadCountChat(it) }
        val basket = viewModel.getCurrentBasketSync()
        basket?.let {
            basket.viewType = null
            viewModel.updateBasket(basket)
        }
    }

    private fun sendMessageFirebase(message: String) {
        val messageData = HashMap<String, Any>()
        messageData["time"] = System.currentTimeMillis() * -1
        messageData["user_id"] = myUser.id ?: 0
        messageData["user_message"] = message
        messageData["username"] = myUser.username!!

        replyMessage?.let {
            val replyData = HashMap<String, Any>()
            replyData["message_key"] = it.key ?: ""
            replyData["time"] = it.messageTime ?: ""
            replyData["user_id"] = it.userId ?: 0
            replyData["user_message"] = it.message ?: ""
            replyData["username"] = it.userName ?: ""

            messageData["reply_message"] = replyData
        }
        replyMessage = null

        firebaseDatabase.getReference("chats").child(chatId!!).push().setValue(messageData)
    }

    private fun setRecyclerViewScrollListener() {
        binding.messageRv.addOnScrollListener(nextScrollListener)
    }

    private fun changeDateTxt() {
        val layoutManager = binding.messageRv.layoutManager as LinearLayoutManager?
        val lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
        if (lastVisibleItemPosition >= 0 && lastVisibleItemPosition < messages.size) {
            val message = messages[lastVisibleItemPosition]
            message.messageTime?.let {
                binding.dateTxt.visibility = View.VISIBLE
                binding.dateTxt.text = Constants.DATE_FORMAT_5.format(Date(it))
            }
        }
    }
}