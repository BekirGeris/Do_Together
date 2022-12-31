package com.example.dotogether.view.adapter.holder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.MemberAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog

class TargetHolder(view: View, val layoutInflater: LayoutInflater) : BaseHolder(view), View.OnClickListener {

    private lateinit var target: Target
    private val binding = ItemTargetBinding.bind(view)
    private val context = binding.root.context

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    private lateinit var memberAdapter: MemberAdapter

    init {
        initViews()
    }

    private fun initViews() {
        binding.membersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        dialog.setContentView(dialogBinding.root)

        binding.holderView.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.userImage.setOnClickListener(this)
        binding.postUserName.setOnClickListener(this)
        binding.postTime.setOnClickListener(this)
        binding.postImage.setOnClickListener(this)
        binding.like.setOnClickListener(this)
        binding.join.setOnClickListener(this)

        dialogBinding.save.visibility = View.VISIBLE
        dialogBinding.share.visibility = View.VISIBLE
        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.save.setOnClickListener(this)
        dialogBinding.share.setOnClickListener(this)
        dialogBinding.delete.setOnClickListener(this)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun bind(target: Target) {
        this.target = target
        val user = target.admin
        binding.targetTitle.text = target.target
        binding.description.text = target.description

        target.is_joined?.let {
            if (it) {
                binding.join.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_add_circle_24), null, null, null)
            } else {
                binding.join.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_add_circle_outline_24), null, null, null)
            }
        }

        target.is_liked?.let {
            if (it) {
                binding.like.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_thumb_up_alt_24), null, null, null)
            } else {
                binding.like.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_baseline_thumb_up_off_alt_24), null, null, null)
            }
        }

        target.users?.let {
            memberAdapter = MemberAdapter(it, false)
            binding.membersRv.adapter = memberAdapter
        }

        user?.let {
            binding.postUserName.text = it.username
            RuntimeHelper.glideForPersonImage(context).load(it.img).into(binding.userImage)
        }

        if (target.img != null) {
            RuntimeHelper.glideForImage(context).load(target.img).into(binding.postImage)
        } else {
            binding.postImage.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.holderView, binding.postImage -> {
                goToTargetFragment(navController, target.id!!)
            }
            binding.moreSettingBtn-> {
                dialog.show()
            }
            binding.postUserName, binding.userImage, binding.postTime -> {
                target.admin?.id?.let { goToProfileFragment(navController, it) }
            }
            binding.like -> {
                getOnClickListener().holderListener(
                    binding,
                    if(target.is_liked == false) Constants.MethodType.METHOD_LIKE_TARGET else Constants.MethodType.METHOD_UN_LIKE_TARGET,
                    adapterPosition - 1)
            }
            binding.join -> {
                getOnClickListener().holderListener(
                    binding,
                    if(target.is_joined == false) Constants.MethodType.METHOD_JOIN_TARGET else Constants.MethodType.METHOD_UN_JOIN_TARGET,
                    adapterPosition - 1)
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
        }
    }
}