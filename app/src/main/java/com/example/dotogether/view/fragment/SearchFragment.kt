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
class SearchFragment : BaseFragment(), View.OnClickListener, HolderListener.TargetHolderListener{

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

        userAdapter = UserAdapter(users)
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
                    isSearching = false
                    users.clear()
                    resource.data?.let { users.addAll(it) }
                    userAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
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
                    isSearching = false
                    targets.clear()
                    resource.data?.let { targets.addAll(it) }
                    targetAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    isSearching = false
                    showToast(resource.message)
                }
                is Resource.Loading -> {

                }
            }
            changeErrorViewVisibility()
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

    }

    override fun join(binding: ItemTargetBinding, target: Target) {

    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {

    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {

    }
}