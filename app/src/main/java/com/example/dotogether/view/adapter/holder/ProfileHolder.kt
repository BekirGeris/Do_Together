package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryShow
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
    private val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    init {
        initViews()
    }

    private fun initViews() {
        bottomSheetDialog.setContentView(dialogBinding.root)

        binding.backgroundImage.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.moreSettingBtn.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
        binding.description.setOnClickListener(this)
        binding.followersLyt.setOnClickListener(this)
        binding.followingLyt.setOnClickListener(this)
        binding.followBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)

        dialogBinding.appShare.visibility = View.VISIBLE
        dialogBinding.userInfo.visibility = View.VISIBLE
        dialogBinding.editBackgroundImage.visibility = View.VISIBLE
        dialogBinding.editProfileImage.visibility = View.VISIBLE
        dialogBinding.editPassword.visibility = View.VISIBLE
        dialogBinding.logout.visibility = View.VISIBLE
        dialogBinding.deleteMyAccount.visibility = View.VISIBLE
        dialogBinding.appShare.setOnClickListener(this)
        dialogBinding.userInfo.setOnClickListener(this)
        dialogBinding.editBackgroundImage.setOnClickListener(this)
        dialogBinding.editProfileImage.setOnClickListener(this)
        dialogBinding.editPassword.setOnClickListener(this)
        dialogBinding.logout.setOnClickListener(this)
        dialogBinding.deleteMyAccount.setOnClickListener(this)

        if (listener.isOtherActivity()) {
            binding.backBtn.visibility = View.VISIBLE
        }
    }

    fun bind(user: User) {
        this.user = user

        if (listener.isMe(binding, user)) {
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
            binding.backgroundImage.background = ContextCompat.getDrawable(context, R.drawable.target_background)
        }

        binding.followBtn.text = context.getString(if (user.is_followed == true) R.string.un_follow else R.string.follow)
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            //e.printStackTrace()
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
                bottomSheetDialog.tryShow()
            }
            binding.profileImage -> {
                listener.showReels(binding, user)
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
                goToChatFragment(navController, user.chat_id, OtherUser(user), false)
            }
            dialogBinding.userInfo -> {
                goToUserEditFragment(navController, user)
                bottomSheetDialog.dismiss()
            }
            dialogBinding.logout -> {
                bottomSheetDialog.dismiss()
                listener.logout(binding, user)
            }
            dialogBinding.editBackgroundImage -> {
                bottomSheetDialog.dismiss()
                listener.backgroundImageEdit(binding, user)
            }
            dialogBinding.editProfileImage -> {
                bottomSheetDialog.dismiss()
                listener.profileImageEdit(binding, user)
            }
            dialogBinding.editPassword -> {
                goToPasswordEditFragment(navController, user)
                bottomSheetDialog.dismiss()
            }
            dialogBinding.deleteMyAccount -> {
                bottomSheetDialog.dismiss()
                listener.deleteMyAccount(binding, user)
            }
            dialogBinding.appShare -> {
                bottomSheetDialog.dismiss()
                RuntimeHelper.shareAppLink(context)
            }
        }
    }
}