package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileHolder(val view: View, val layoutInflater: LayoutInflater) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemProfileBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    private val dialogBinding = BottomSheetBinding.inflate(layoutInflater)
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
        binding.fallowBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
        binding.backgroundEditBtn.setOnClickListener(this)
        binding.profileEditBtn.setOnClickListener(this)

        dialogBinding.save.visibility = View.GONE
        dialogBinding.share.visibility = View.GONE
        dialogBinding.delete.visibility = View.GONE
        dialogBinding.edit.setOnClickListener(this)
        dialogBinding.clearChat.visibility = View.GONE
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
                navController.popBackStack().let {
                    if (!it) {
                        getOnClickListener().holderListener(binding, 2, adapterPosition)
                    }
                }
            }
            binding.moreSettingBtn -> {
                dialog.show()
            }
            binding.profileImage -> {

            }
            binding.description -> {

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
        }
    }

    private fun invertEditVisibility() {
        binding.backgroundEditBtn.visibility = if (binding.backgroundEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.profileEditBtn.visibility = if (binding.profileEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }
}