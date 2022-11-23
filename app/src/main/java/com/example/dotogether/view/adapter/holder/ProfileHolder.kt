package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileHolder(val view: View, val layoutInflater: LayoutInflater) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemProfileBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    init {
        initViews()
    }

    private fun initViews() {
        dialog.setContentView(dialogBinding.root)

        binding.backgroundImage.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
        binding.description.setOnClickListener(this)
        binding.followersLyt.setOnClickListener(this)
        binding.followingLyt.setOnClickListener(this)
        binding.fallowBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
        binding.backgroundEditBtn.setOnClickListener(this)
        binding.profileEditBtn.setOnClickListener(this)
        binding.closeBtn.setOnClickListener(this)
        binding.confirmBtn.setOnClickListener(this)

        dialogBinding.edit.visibility = View.VISIBLE
        dialogBinding.logout.visibility = View.VISIBLE
        dialogBinding.edit.setOnClickListener(this)
        dialogBinding.logout.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user
    }

    override fun onClick(v: View?) {
        val navController = view.findNavController()
        when(v) {
            binding.backgroundImage -> {

            }
            binding.backBtn -> {
                if (!navController.popBackStack()) {
                    getOnClickListener().holderListener(binding, 2, adapterPosition)
                }
            }
            binding.moreSettingBtn -> {
                dialog.show()
            }
            binding.profileImage -> {

            }
            binding.description -> {

            }
            binding.followersLyt -> {
                goToFollowsFragment(navController)
            }
            binding.followingLyt -> {
                goToFollowsFragment(navController)
            }
            binding.fallowBtn -> {

            }
            binding.messageBtn -> {
                goToChatFragment(navController)
            }
            binding.backgroundEditBtn -> {
                getOnClickListener().holderListener(binding, 0, adapterPosition)
                invertEditVisibility()
            }
            binding.profileEditBtn -> {
                getOnClickListener().holderListener(binding, 1, adapterPosition)
                invertEditVisibility()
            }
            dialogBinding.edit -> {
                invertEditVisibility()
                dialog.hide()
            }
            dialogBinding.logout -> {
                getOnClickListener().holderListener(binding, 2, adapterPosition)
            }
            binding.closeBtn -> {
                invertEditVisibility()
            }
            binding.confirmBtn -> {
                invertEditVisibility()
            }
        }
    }

    private fun invertEditVisibility() {
        binding.backgroundEditBtn.visibility = if (binding.backgroundEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.profileEditBtn.visibility = if (binding.profileEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.closeBtn.visibility = if (binding.closeBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.confirmBtn.visibility = if (binding.confirmBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.description.isFocusableInTouchMode = !binding.description.isEnabled
        binding.description.isFocusable = !binding.description.isEnabled
        binding.description.isCursorVisible = !binding.description.isEnabled
        binding.description.isEnabled = !binding.description.isEnabled
    }
}