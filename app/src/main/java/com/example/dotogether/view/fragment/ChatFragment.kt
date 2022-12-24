package com.example.dotogether.view.fragment

import android.os.Bundle
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
import com.example.dotogether.view.adapter.MessageAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val messages = arrayListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    var isGroup = false

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

    private fun initViews() {
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

        for (i in 1..100) {
            if (i % 3 == 0) {
                messages.add(Message("Bekir Geriş",
                    "${i % 24}:${i % 60}",
                    "Bu bir test mesajıdır sdcfvsdcvsd sdfvbsdv sdv sdfv sdv svf sv sdv swdv sv sdfv sbfsfbv sdv sdv swdv sdf svdgsdv sdd dsv ssv ssdvwsd ssv s s sf sfsf sf.",
                    true
                ))
            } else {
                messages.add(Message("Ömer Abi",
                    "${i % 24}:${i % 60}",
                    "Bu bir test mesajıdır sdcfvsdcvsd sdfvbsdv sdv sdfv sdv svf sv sdv swdv sv sdfv sbfsfbv sdv sdv swdv sdf svdgsdv sdd dsv ssv ssdvwsd ssv s s sf sfsf sf.",
                    false
                ))
            }
        }
        messageAdapter = MessageAdapter(messages, isGroup)

        binding.messageRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.messageRv.adapter = messageAdapter
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

                }
                dialogBinding.clearChat -> {
                    bottomSheetDialog.hide()
                }
                else -> {}
            }
        }
    }
}