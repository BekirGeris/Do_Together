package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.ArrayList

class TargetHolder(val view: View, val layoutInflater: LayoutInflater) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemTargetBinding.bind(view)
    private val context = binding.root.context

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    private var memberAdapter: MemberAdapter
    private val members = ArrayList<User>()

    init {
        initViews()
        for (i in 1..100) {
            members.add(User())
        }

        memberAdapter = MemberAdapter(members, false)
    }

    private fun initViews() {
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

    fun bind(target: Target) {
        binding.membersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.membersRv.adapter = memberAdapter
        binding.postImage.visibility = View.VISIBLE

        //todo: test bloc
        binding.postImage.setImageDrawable(when(target.url) {
            1 -> {
                ContextCompat.getDrawable(context, R.drawable.work)
            }
            2 -> {
                ContextCompat.getDrawable(context, R.drawable.ic_virus)
            }
            3 -> {
                ContextCompat.getDrawable(context, R.drawable.mechanic)
            }
            4 -> {
                ContextCompat.getDrawable(context, R.drawable.ic_watering_the_plants)
            }
            else -> {
                binding.postImage.visibility = View.GONE
                null
            }
        })
    }

    override fun onClick(v: View?) {
        val navController = view.findNavController()
        when(v) {
            binding.holderView, binding.postImage -> {
                goToTargetFragment(navController)
            }
            binding.moreSettingBtn-> {
                dialog.show()
            }
            binding.postUserName, binding.userImage, binding.postTime -> {
                 goToProfileFragment(navController)
            }
            binding.like -> {

            }
            binding.join -> {

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