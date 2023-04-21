package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentDiscoverBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.DiscoverAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.DiscoverViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DiscoverFragment @Inject constructor() : BaseFragment(), View.OnClickListener, HolderListener.TargetHolderListener {

    private val viewModel: DiscoverViewModel by viewModels()
    private lateinit var binding: FragmentDiscoverBinding

    private val discovers = ArrayList<Discover>()
    private lateinit var discoverAdapter: DiscoverAdapter

    private lateinit var bottomSheetSettingBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetSettingDialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDiscoverBinding.inflate(layoutInflater)
        bottomSheetSettingBinding = BottomSheetSettingBinding.inflate(layoutInflater)
        bottomSheetSettingDialog = BottomSheetDialog(bottomSheetSettingBinding.root.context, R.style.BottomSheetDialogTheme)

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
        bottomSheetSettingDialog.setContentView(bottomSheetSettingBinding.root)

        discoverAdapter = DiscoverAdapter(discovers, this)
        binding.discoverRv.adapter = discoverAdapter

        binding.searchBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)

        bottomSheetSettingBinding.addTagOfLike.visibility = View.VISIBLE
        bottomSheetSettingBinding.addTagOfLike.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.allDiscover.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.activityErrorView.visibility = View.GONE
                    resource.data?.let {
                        discovers.clear()
                        discovers.addAll(it)
                        discoverAdapter.notifyDataSetChanged()
                    }
//                    dialog.hide()
                }
                is Resource.Error -> {
                    binding.activityErrorView.visibility = View.VISIBLE
//                    dialog.hide()
                    showToast(resource.message)
                }
                is Resource.Loading -> {
//                    dialog.show()
                }
                else -> {}
            }
        }
        viewModel.getAllDiscover()
        viewModel.updateTarget.observe(viewLifecycleOwner) {resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { updateTarget ->
                        discovers.map {discover ->
                            discover.targets?.map {
                                if (it.id == updateTarget.id) {
                                    it.is_joined = updateTarget.is_joined
                                    it.is_liked = updateTarget.is_liked
                                    it.users = updateTarget.users
                                }
                            }
                        }
                        discoverAdapter.notifyDataChangedForTargets()
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

    override fun like(binding: ItemTargetBinding, target: Target) {
        viewModel.likeTarget(target.id!!)
    }

    override fun join(binding: ItemTargetBinding, target: Target) {
        viewModel.joinTarget(target.id!!)
    }

    override fun unLike(binding: ItemTargetBinding, target: Target) {
        viewModel.unLikeTarget(target.id!!)
    }

    override fun unJoin(binding: ItemTargetBinding, target: Target) {
        viewModel.unJoinTarget(target.id!!)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.searchBtn -> {
                goToSearchFragment()
            }
            binding.moreSettingBtn -> {
                bottomSheetSettingDialog.show()
            }
            bottomSheetSettingBinding.addTagOfLike -> {
                bottomSheetSettingDialog.dismiss()
                goToAddTagFragment()
            }
        }
    }

    override fun goToRecyclerViewTop() {
        binding.discoverRv.smoothScrollToPosition(0)
    }

    override fun isDiscoverFragment(): Boolean {
        return true
    }
}