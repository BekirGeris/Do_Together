package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemMemberBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper

class MemberHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMemberBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.userImage.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user

        user.img?.let {
            RuntimeHelper.glideForPersonImage(context).load(it).into(binding.userImage)
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
            binding.userImage -> {
                user.id?.let { goToProfileFragment(navController, it) }
            }
        }
    }
}