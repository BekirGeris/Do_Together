package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.User

class UserHolder(view: View) : BaseHolder(view), View.OnClickListener {

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
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.holderView -> {
                goToProfileFragment(navController)
            }
        }
    }
}