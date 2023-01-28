package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentChatListBinding
import com.example.dotogether.model.response.MyChatsResponse
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.ChatAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListChatFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatListBinding

    private lateinit var chatAdapter: ChatAdapter
    private val chats = arrayListOf<MyChatsResponse>()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)

        chatAdapter = ChatAdapter(chats)

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
        binding.chatsRv.adapter = chatAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myChats.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    dialog.hide()
                    resource.data?.let { list ->
                        chats.clear()
                        chats.addAll(list)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
            }
        }
        viewModel.myChats()
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when (v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                else -> {}
            }
        }
    }
}