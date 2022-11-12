package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemBigMemberBinding
import com.example.dotogether.model.User

class BigMemberHolder(val view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemBigMemberBinding.bind(view)
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
        val navController = view.findNavController()
        when(v) {
            binding.userImage -> {
                goToProfileFragment(navController)
            }
        }
    }
}