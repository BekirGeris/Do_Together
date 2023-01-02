package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileHolder(
    view: View,
    val layoutInflater: LayoutInflater,
    private val listener: HolderListener.ProfileHolderListener
) : BaseHolder(view), View.OnClickListener {

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
        binding.followBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
        binding.backgroundEditBtn.setOnClickListener(this)
        binding.profileEditBtn.setOnClickListener(this)
        binding.descriptionCloseBtn.setOnClickListener(this)
        binding.descriptionConfirmBtn.setOnClickListener(this)

        dialogBinding.edit.visibility = View.VISIBLE
        dialogBinding.logout.visibility = View.VISIBLE
        dialogBinding.edit.setOnClickListener(this)
        dialogBinding.logout.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user

        if (listener.itIsMe(binding, user)) {
            binding.moreSettingBtn.visibility = View.VISIBLE
            binding.btnLayout.visibility = View.GONE
        }

        binding.userNameTxt.text = user.username
        binding.description.setText(user.description)
        binding.followersNumberTxt.text = user.follower_number.toString()
        binding.followingNumberTxt.text = user.following_number.toString()
        RuntimeHelper.glideForPersonImage(context).load(user.img).into(binding.profileImage)

        if (user.background_img != null) {
            RuntimeHelper.glideForImage(context).load(user.background_img).into(binding.backgroundImage)
        } else {
            binding.backgroundImage.background = ContextCompat.getDrawable(context, R.drawable.pilgrim)
        }

        binding.followBtn.text = context.getString(if (user.is_followed == true) R.string.un_follow else R.string.follow)
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (v) {
            binding.backgroundImage -> {

            }
            binding.backBtn -> {
                navController?.let {
                    if (!it.popBackStack()) {
                        listener.finishActivity(binding, user)
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
            binding.followersLyt -> {
                user.id?.let { goToFollowsFragment(navController, it, 1) }
            }
            binding.followingLyt -> {
                user.id?.let { goToFollowsFragment(navController, it, 2) }
            }
            binding.followBtn -> {
                if (user.is_followed == true)
                    listener.unFollow(binding, user)
                else
                    listener.follow(binding, user)
            }
            binding.messageBtn -> {
                goToChatFragment(navController)
            }
            binding.backgroundEditBtn -> {
                listener.backgroundImageEdit(binding, user)
                invertEditVisibility()
            }
            binding.profileEditBtn -> {
                listener.profileImageEdit(binding, user)
                invertEditVisibility()
            }
            dialogBinding.edit -> {
                invertEditVisibility()
                dialog.dismiss()
            }
            dialogBinding.logout -> {
                listener.logout(binding, user)
            }
            binding.descriptionCloseBtn -> {
                binding.description.setText(user.description)
                invertEditVisibility()
            }
            binding.descriptionConfirmBtn -> {
                invertEditVisibility()
                user.description = binding.description.text.toString()
                listener.descriptionImageEdit(binding, user)
            }
        }
    }

    private fun invertEditVisibility() {
        binding.backgroundEditBtn.visibility = if (binding.backgroundEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.profileEditBtn.visibility = if (binding.profileEditBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.descriptionCloseBtn.visibility = if (binding.descriptionCloseBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.descriptionConfirmBtn.visibility = if (binding.descriptionConfirmBtn.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        binding.description.isFocusableInTouchMode = !binding.description.isEnabled
        binding.description.isFocusable = !binding.description.isEnabled
        binding.description.isCursorVisible = !binding.description.isEnabled
        binding.description.isEnabled = !binding.description.isEnabled
    }
}