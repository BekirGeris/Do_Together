package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.User

class UserHolder(val view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemUserBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user
    }

    override fun onClick(v: View?) {
        val navController = view.findNavController()
        when(v) {
            binding.holderView -> {
                goToProfileFragment(navController)
            }
        }
    }
}