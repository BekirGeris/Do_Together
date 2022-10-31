package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentChatBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.MessageAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatBinding

    private val messages = arrayListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)
        binding.chatsUserImage.setOnClickListener(this)
        binding.chatsUserName.setOnClickListener(this)
        binding.lastSeenTxt.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.attachBtn.setOnClickListener(this)
        binding.sendMessageBtn.setOnClickListener(this)

        for (i in 1..1000) {
            messages.add(Message("${i % 24}:${i % 60}",
                "Bu bir test mesajıdır sdcfvsdcvsd sdfvbsdv sdv sdfv sdv svf sv sdv swdv sv sdfv sbfsfbv sdv sdv swdv sdf svdgsdv sdd dsv ssv ssdvwsd ssv s s sf sfsf sf.",
                i % 2 == 0
            ))
        }
        messageAdapter = MessageAdapter(messages)

        binding.messageRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        binding.messageRv.adapter = messageAdapter

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        val navController = v?.findNavController()
        var action: NavDirections
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    navController.popBackStack().let {
                        if (!it) {
                            activity?.finish()
                        }
                    }
                }
                binding.chatsUserImage, binding.chatsUserName, binding.lastSeenTxt -> {
                    action = ChatFragmentDirections.actionChatFragmentToProfileFragment()
                    navController.navigate(action)
                }
                binding.moreSettingBtn -> {

                }
                binding.attachBtn -> {

                }
                binding.sendMessageBtn -> {

                }
            }
        }
    }
}