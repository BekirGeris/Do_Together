package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemMemberBinding
import com.example.dotogether.model.User

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
                goToProfileFragment(navController)
            }
        }
    }
}