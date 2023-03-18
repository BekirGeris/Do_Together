package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daimajia.swipe.SwipeLayout
import com.example.dotogether.R
import com.example.dotogether.databinding.ItemMemberForListBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.holderListener.HolderListener

class ListMemberHolder(view: View, private val listener: HolderListener.ListMemberHolderListener) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMemberForListBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.userHolder.holderView.setOnClickListener(this)
        binding.userHolder.followOrUnfollowBtn.setOnClickListener(this)
        binding.deleteMember.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user

        RuntimeHelper.glideForPersonImage(context).load(user.img).into(binding.userHolder.userImage)

        binding.userHolder.username.text = user.username
        binding.userHolder.name.text = user.name
        binding.userHolder.followOrUnfollowBtn.setImageResource(if (user.is_followed == true) R.drawable.baseline_person_remove_24 else R.drawable.ic_baseline_person_add_alt_24 )

        binding.userHolder.followOrUnfollowBtn.visibility = if (listener.isMe(user)) View.GONE else View.VISIBLE

        binding.swipeLayout.showMode = SwipeLayout.ShowMode.LayDown
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.userHolder.holderView -> {
                user.id?.let { goToProfileFragment(navController, it) }
            }
            binding.userHolder.followOrUnfollowBtn -> {
                if (user.is_followed == true) {
                    listener.unFollow(user)
                } else {
                    listener.follow(user)
                }
            }
            binding.deleteMember -> {
                listener.deleteMember(user)
            }
        }
    }
}