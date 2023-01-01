package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.FragmentTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.viewmodel.TargetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TargetFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetBinding

    private lateinit var dialogBinding: BottomSheetSettingBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var memberAdapter: MemberAdapter

    private var justOneWork = true

    var targetId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTargetBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (justOneWork) {
            initObserve()
            justOneWork = false
        }
    }

    private fun initViews() {
        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.backBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.groupMessageBtn.setOnClickListener(this)
        binding.joinBtn.setOnClickListener(this)

        dialogBinding.save.visibility = View.VISIBLE
        dialogBinding.share.visibility = View.VISIBLE
        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.edit.visibility = View.VISIBLE
        dialogBinding.save.setOnClickListener(this)
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.delete.setOnClickListener(this)
        dialogBinding.edit.setOnClickListener(this)

        binding.memberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        targetId = arguments?.getInt("targetId")
    }

    private fun initObserve() {
        viewModel.target.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { target ->
                        setViewWithTarget(target)
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        viewModel.likeJoinLiveData.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { target ->
                        setViewWithTarget(target)
                    }
                    dialog.hide()
                }
                is Resource.Error -> {
                    dialog.hide()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
                else -> {}
            }
        }
        targetId?.let {
            viewModel.getTarget(it)
        }
    }

    private fun setViewWithTarget(target: Target) {
        target.users?.let { members ->
            memberAdapter = MemberAdapter(members, true)
            binding.memberRv.adapter = memberAdapter
        }

        binding.joinBtn.visibility = if (target.is_joined == false) View.VISIBLE else View.GONE
        binding.groupMessageBtn.visibility = if (target.is_joined == true) View.VISIBLE else View.GONE

        target.target?.let {
            binding.target.text = it
        }

        target.description?.let {
            binding.description.text = it
        }

        target.period?.let {
            binding.period.text = it
        }

        target.start_date?.let {
            binding.startDate.text = it
        }

        target.end_date?.let {
            binding.endDate.text = if (it == getString(R.string.forever_date)) getString(R.string.forever) else it
        }

        if (target.img != null) {
            RuntimeHelper.glideForImage(requireContext()).load(target.img).into(binding.targetImage)
        } else {
            binding.targetImage.background = ContextCompat.getDrawable(requireContext(), R.drawable.pilgrim)
        }
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                binding.moreSettingBtn -> {
                    bottomSheetDialog.show()
                }
                binding.groupMessageBtn -> {
                    navController.navigate(TargetFragmentDirections.actionChatFragment(true))
                }
                binding.joinBtn -> {
                    viewModel.joinTarget(targetId!!)
                }
                dialogBinding.save -> {
                    bottomSheetDialog.hide()
                }
                dialogBinding.share -> {
                    bottomSheetDialog.hide()
                }
                dialogBinding.delete -> {
                    bottomSheetDialog.hide()
                }
                dialogBinding.edit -> {
                    bottomSheetDialog.hide()
                }
                else -> {}
            }
        }
    }
}