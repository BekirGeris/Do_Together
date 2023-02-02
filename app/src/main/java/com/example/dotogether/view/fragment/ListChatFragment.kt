package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentChatListBinding
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.model.response.ChatResponse
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.ChatAdapter
import com.example.dotogether.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListChatFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var binding: FragmentChatListBinding

    private lateinit var chatAdapter: ChatAdapter
    private val chats = arrayListOf<ChatResponse>()

    private var isSearching = false

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
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.backBtn.setOnClickListener(this)

        chatAdapter = ChatAdapter(chats)

        binding.chatsRv.layoutManager = LinearLayoutManager(context)
        binding.chatsRv.adapter = chatAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (!isSearching) {
                        viewModel.searchMyChats(SearchRequest(newText))
                        isSearching = true
                    }
                } else {
                    viewModel.myChats()
                }
                return true
            }
        })

        binding.swipeLyt.setOnRefreshListener {
            viewModel.myChats()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.myChats.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                    resource.data?.let { list ->
                        chats.clear()
                        chats.addAll(list)
                        chatAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing && chats.isEmpty()) {
                        dialog.shoe()
                    }
                }
                else -> {}
            }
        }
        viewModel.searchMyChats.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { list ->
                        chats.clear()
                        chats.addAll(list)
                        chatAdapter.notifyDataSetChanged()
                        isSearching = false
                    }
                }
                is Resource.Error -> {
                    isSearching = false
                    showToast(resource.message)
                }
                is Resource.Loading -> {

                }
                else -> {}
            }
        }
        viewModel.getCurrentBasket().observe(viewLifecycleOwner) {basket ->
            basket?.let {
                Log.d(RuntimeHelper.TAG, "basket: $it")
            }
            viewModel.myChats()
        }
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