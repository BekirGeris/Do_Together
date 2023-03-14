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
import com.example.dotogether.databinding.FragmentSearchBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.UserAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class SearchFragment : BaseFragment(), View.OnClickListener, HolderListener.TargetHolderListener, HolderListener.UserHolderListener {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchBinding

    private lateinit var userAdapter: UserAdapter
    private lateinit var targetAdapter: TargetAdapter

    private val users = ArrayList<User>()
    private val targets = ArrayList<Target>()

    private var isShowUser = true
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSearchBinding.inflate(layoutInflater)

        initViews();
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

    @SuppressLint("NotifyDataSetChanged")
    private fun initViews() {
        binding.searchView.onActionViewExpanded()
        binding.backBtn.setOnClickListener(this)

        binding.activityErrorView.visibility = View.VISIBLE
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (!isSearching) {
                        viewModel.searchUser(SearchRequest(newText))
                        viewModel.searchTarget(SearchRequest(newText))
                        binding.linearIndicator.visibility = View.VISIBLE
                        isSearching = true
                    }
                } else {
                    users.clear()
                    targets.clear()
                    userAdapter.notifyDataSetChanged()
                    targetAdapter.notifyDataSetChanged()
                    binding.activityErrorView.visibility = View.VISIBLE
                }
                return true
            }
        })

        userAdapter = UserAdapter(users, this)
        targetAdapter = TargetAdapter(targets, this)

        binding.searchRv.layoutManager = LinearLayoutManager(context)
        binding.searchRv.adapter = userAdapter

        binding.personsTargetsRadioGrp.setOnCheckedChangeListener { radioGroup, i ->
            if (i == binding.personsBtn.id) {
                isShowUser = true
                binding.searchRv.adapter = userAdapter
            } else if (i == binding.targetsBtn.id) {
                isShowUser = false
                binding.searchRv.adapter = targetAdapter
            }
            changeErrorViewVisibility()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.users.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                    users.clear()
                    resource.data?.let { users.addAll(it) }
                    userAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                    showToast(resource.message)
                }
                is Resource.Loading -> {

                }
            }
            changeErrorViewVisibility()
        }
        viewModel.targets.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    targets.clear()
                    resource.data?.let { targets.addAll(it) }
                    targetAdapter.notifyDataSetChanged()
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                }
                is Resource.Error -> {
                    binding.linearIndicator.visibility = View.GONE
                    isSearching = false
                    showToast(resource.message)
                }
                is Resource.Loading -> {

                }
            }
            changeErrorViewVisibility()
        }
        viewModel.updateUser.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    resource.data?.let { updateUser ->
                        users.map { if (updateUser.id == it.id) { it.is_followed = updateUser.is_followed } }
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
        viewModel.updateTarget.observe(viewLifecycleOwner) {resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { updateTarget ->
                        targets.map {
                            if (it.id == updateTarget.id) {
                                it.is_liked = updateTarget.is_liked
                                it.is_joined = updateTarget.is_joined
                                it.users = updateTarget.users
                            }
                        }
                        targetAdapter.notifyDataSetChanged()
                    }
                    dialog.hide()
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
    }

    private fun changeErrorViewVisibility() {
        if (isShowUser) {
            if (users.isNotEmpty()) {
                binding.activityErrorView.visibility = View.GONE
            } else {
                binding.activityErrorView.visibility = View.VISIBLE
            }
        } else {
            if (targets.isNotEmpty()) {
                binding.activityErrorView.visibility = View.GONE
            } else {
                binding.activityErrorView.visibility = View.VISIBLE
            }
        }
    }

    override fun like(binding: ItemTargetBinding, target: Target) {
        this.binding.searchView.clearFocus()
        target.id?.let { viewModel.likeTarget(it) }
    }

    override fun join(binding: ItemTargetBinding, target: Target) {
        this.binding.searchView.clearFocus()
        target.id?.let { viewModel.joinTarget(it) }
    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {
        this.binding.searchView.clearFocus()
        target.id?.let { viewModel.unLikeTarget(it) }
    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {
        this.binding.searchView.clearFocus()
        target.id?.let { viewModel.unJoinTarget(it) }
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