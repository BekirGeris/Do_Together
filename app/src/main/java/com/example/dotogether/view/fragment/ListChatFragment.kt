package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentChatListBinding
import com.example.dotogether.model.Chat
import com.example.dotogether.view.adapter.ChatAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListChatFragment : BaseFragment() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatListBinding

    private lateinit var chatAdapter: ChatAdapter
    private val chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentChatListBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        for (i in 1..100) {
            chats.add(Chat())
        }
        chatAdapter = ChatAdapter(chats)

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
        binding.chatsRv.adapter = chatAdapter
        println("bekbek ${viewModel.text.value}")
    }
}