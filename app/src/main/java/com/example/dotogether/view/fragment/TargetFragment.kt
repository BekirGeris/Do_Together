package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetBinding
import com.example.dotogether.databinding.FragmentTargetBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import com.example.dotogether.viewmodel.TargetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.ArrayList

class TargetFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: TargetViewModel by viewModels()
    private lateinit var binding: FragmentTargetBinding

    private lateinit var dialogBinding: BottomSheetBinding
    private lateinit var dialog: BottomSheetDialog

    private lateinit var memberAdapter: MemberAdapter
    private val members = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTargetBinding.inflate(layoutInflater)
        dialogBinding = BottomSheetBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(dialogBinding.root.context, R.style.BottomSheetDialogTheme)
        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        dialog.setContentView(dialogBinding.root)

        binding.backBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.groupMessageBtn.setOnClickListener(this)
        binding.joinBtn.setOnClickListener(this)

        dialogBinding.save.setOnClickListener(this)
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.delete.setOnClickListener(this)
        dialogBinding.edit.setOnClickListener(this)
        dialogBinding.clearChat.visibility = View.GONE

        for (i in 1..100) {
            members.add(User())
        }

        memberAdapter = MemberAdapter(members, true)

        binding.memberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.memberRv.adapter = memberAdapter

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when(v) {
                binding.backBtn -> {
                    navController.popBackStack().let {
                        if (!it) {
                            activity?.finish()
                        }
                    }
                }
                binding.moreSettingBtn -> {
                    dialog.show()
                }
                binding.groupMessageBtn -> {
                    navController.navigate(TargetFragmentDirections.actionChatFragment(true))
                }
                binding.joinBtn -> {

                }
                dialogBinding.save -> {
                    dialog.hide()
                }
                dialogBinding.share -> {
                    dialog.hide()
                }
                dialogBinding.delete -> {
                    dialog.hide()
                }
                dialogBinding.edit -> {
                    dialog.hide()
                }
            }
        }
    }
}