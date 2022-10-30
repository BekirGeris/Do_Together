package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.model.User

class ProfileHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemProfileBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.backgroundImage.setOnClickListener(this)
        binding.threeBtn.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
        binding.description.setOnClickListener(this)
        binding.fallowBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user
    }

    override fun onClick(v: View?) {
        val navController = v?.findNavController()
        when(v) {
            binding.backgroundImage -> {

            }
            binding.threeBtn -> {

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
        }
    }
}