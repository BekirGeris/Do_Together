package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.R
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.holderListener.HolderListener

class UserHolder(view: View, private val listener: HolderListener.UserHolderListener) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemUserBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
        binding.followOrUnfollowBtn.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user

        RuntimeHelper.glideForPersonImage(context).load(user.img).into(binding.userImage)

        binding.username.text = user.username
        binding.name.text = user.name
        binding.followOrUnfollowBtn.setImageResource(if (user.is_followed == true) R.drawable.baseline_person_remove_24 else R.drawable.ic_baseline_person_add_alt_24 )

        binding.followOrUnfollowBtn.visibility = if (listener.isMe(user)) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.holderView -> {
                user.id?.let { goToProfileFragment(navController, it) }
            }
            binding.followOrUnfollowBtn -> {
                if (user.is_followed == true) {
                    listener.unFollow(user)
                } else {
                    listener.follow(user)
                }
            }
        }
    }
}