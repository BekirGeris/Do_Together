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
import com.example.dotogether.R
import com.example.dotogether.databinding.FragmentTargetMembersBinding
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.ListMemberAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.viewmodel.TargetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TargetMembersFragment : BaseFragment(), View.OnClickListener, HolderListener.ListMemberHolderListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetMembersBinding

    private lateinit var memberAdapter: ListMemberAdapter

    private val users = ArrayList<User>()
    private var targetId: Int? = null
    private var isAdmin: Boolean = false
    private var myUserId: Int? = null

    private var isSearching = false

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1)) {
                targetId?.let {
                    viewModel.getNextMembers(it, pageNo = nextPage)
                }
                binding.memberRv.removeOnScrollListener(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTargetMembersBinding.inflate(layoutInflater)

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

        targetId = arguments?.getInt(Constants.TARGET_ID, -1)
        isAdmin = arguments?.getBoolean(Constants.IS_ADMIN) ?: false

        memberAdapter = ListMemberAdapter(users, this)
        binding.memberRv.layoutManager = LinearLayoutManager(context)
        binding.memberRv.adapter = memberAdapter

        binding.infoTxt.visibility = if (isAdmin) View.VISIBLE else View.GONE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (!isSearching) {
                        targetId?.let {
                            viewModel.searchMembers(SearchRequest(search = newText), it)
                        }
                        isSearching = true
                    }
                } else {
                    getMember()
                }
                return true
            }
        })

        memberAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.activityErrorView.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
            }
        })

        binding.swipeLyt.setOnRefreshListener {
            binding.memberRv.removeOnScrollListener(scrollListener)
            getMember()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.getMyUserFromLocale().observe(viewLifecycleOwner) {
            myUserId = it.id
        }
        viewModel.members.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                            users.clear()
                            users.addAll(list)
                            memberAdapter.notifyDataSetChanged()
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
        viewModel.nextMembers.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    binding.nextProgressBar.visibility = View.GONE
                    resource.data?.let { response ->
                        response.data?.let { list ->
                            users.addAll(list)
                            memberAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                }
                is Resource.Error -> {
                    binding.nextProgressBar.visibility = View.GONE
                    showToast(resource.message)
                }
                is Resource.Loading -> {
                    binding.nextProgressBar.visibility = View.VISIBLE
                    binding.memberRv.scrollToPosition(users.size - 1)
                }
                else -> {}
            }
        }
        viewModel.searchMembers.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { list ->
                        users.clear()
                        users.addAll(list)
                        memberAdapter.notifyDataSetChanged()
                        isSearching = false
                    }
                }
                is Resource.Error -> {
                    showToast(resource.message)
                    isSearching = false
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
                        memberAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    showToast(resource.message)
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
        viewModel.removeUser.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    resource.data?.let { user ->
                        val newList = users.filter { it.id != user.id }
                        users.clear()
                        users.addAll(newList)
                        memberAdapter.notifyDataSetChanged()
                    }
                }
                is Resource.Error -> {
                    showToast(resource.message)
                }
                else -> {}
            }
        }
        getMember()
    }

    private fun getMember() {
        targetId?.let {
            viewModel.getMembers(it)
        }
    }

    private fun setRecyclerViewScrollListener() {
        binding.memberRv.addOnScrollListener(scrollListener)
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

    override fun isMe(user: User): Boolean {
        return myUserId != null && myUserId == user.id
    }

    override fun follow(user: User) {
        this.binding.searchView.clearFocus()
        user.id?.let { viewModel.follow(it) }
    }

    override fun unFollow(user: User) {
        this.binding.searchView.clearFocus()
        user.id?.let { viewModel.unFollow(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteMember(user: User) {
        showAlertDialog(getString(R.string.remove_member_message), object : ConfirmDialogListener {
            override fun cancel() {
                memberAdapter.notifyDataSetChanged()
            }

            override fun confirm() {
                if (targetId != null && user.id != null) {
                    viewModel.removeUserFromTarget(targetId!!, user.id!!)
                }
            }
        })
    }

    override fun isAdmin(): Boolean {
        return isAdmin
    }
}