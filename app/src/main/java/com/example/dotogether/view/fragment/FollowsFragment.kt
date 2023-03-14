package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.FragmentFollowsBinding
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.UserAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.FollowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class FollowsFragment : BaseFragment(), View.OnClickListener, HolderListener.UserHolderListener {

    private val viewModel: FollowsViewModel by viewModels()
    private lateinit var binding: FragmentFollowsBinding

    private lateinit var userAdapter: UserAdapter

    private val users = ArrayList<User>()
    var userId: Int? = null
    var followsType: Int? = null

    private var isSearching = false

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1)) {
                userId?.let {
                    if (followsType == 1) {
                        viewModel.getNextFollowers(it, nextPage)
                    } else {
                        viewModel.getNextFollowings(it, nextPage)
                    }
                }
                binding.followRv.removeOnScrollListener(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFollowsBinding.inflate(layoutInflater)

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

        userId = arguments?.getInt("userId", -1)
        followsType = arguments?.getInt("followsType", -1)

        userAdapter = UserAdapter(users, this)
        binding.followRv.layoutManager = LinearLayoutManager(context)
        binding.followRv.adapter = userAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (!isSearching) {
                        userId?.let {
                            if (followsType == 1) {
                                viewModel.searchFollowers(SearchRequest(newText),  it)
                            } else {
                                viewModel.searchFollowings(SearchRequest(newText),  it)
                            }
                        }
                        isSearching = true
                    }
                } else {
                    getFullUser()
                }
                return true
            }
        })

        userAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.activityErrorView.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.swipeLyt.setOnRefreshListener {
            getFullUser()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.users.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                            users.clear()
                            users.addAll(list)
                            userAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    binding.activityErrorView.visibility = View.VISIBLE
                    binding.swipeLyt.isRefreshing = false
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing) {
                        binding.activityErrorView.visibility = View.GONE
                        dialog.show()
                    }
                }
                else -> {}
            }
        }
        viewModel.nextUsers.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { response ->
                        response.data?.let { list ->
                            users.addAll(list)
                            userAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                else -> {}
            }
        }
        viewModel.followings.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { list ->
                        users.clear()
                        users.addAll(list)
                        userAdapter.notifyDataSetChanged()
                        isSearching = false
                    }
                }
                is Resource.Error -> {
                    showToast(resource.message)
                    isSearching = false
                }
                is Resource.Loading -> {
                }
                else -> {}
            }
        }
        viewModel.followers.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { list ->
                        users.clear()
                        users.addAll(list)
                        userAdapter.notifyDataSetChanged()
                        isSearching = false
                    }
                }
                is Resource.Error -> {
                    showToast(resource.message)
                    isSearching = false
                }
                is Resource.Loading -> {
                }
                else -> {}
            }
        }
        viewModel.updateUser.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    resource.data?.let { user ->
                        val newList = ArrayList<User>()
                        users.mapTo(newList) { if (user.id == it.id) user else it }
                        users.clear()
                        users.addAll(newList)
                        userAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
        getFullUser()
    }

    private fun getFullUser() {
        userId?.let {
            if (followsType == 1) {
                viewModel.getFollowers(it)
            } else {
                viewModel.getFollowings(it)
            }
        }
    }

    private fun setRecyclerViewScrollListener() {
        binding.followRv.addOnScrollListener(scrollListener)
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when (v) {
                binding.backBtn -> {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                else -> {}
            }
        }
    }

    override fun follow(binding: ItemUserBinding, user: User) {
        this.binding.searchView.clearFocus()
        user.id?.let { viewModel.follow(it) }
    }

    override fun unFollow(binding: ItemUserBinding, user: User) {
        this.binding.searchView.clearFocus()
        user.id?.let { viewModel.unFollow(it) }
    }
}