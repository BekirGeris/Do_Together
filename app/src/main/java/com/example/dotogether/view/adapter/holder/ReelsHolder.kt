package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemReelsBinding
import com.example.dotogether.model.Reels

class ReelsHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private var binding = ItemReelsBinding.bind(view)
    private val context = binding.root.context
    private lateinit var reels: Reels

    init {
        initViews()
    }

    private fun initViews() {
        binding.userImage.setOnClickListener(this)
    }

    fun bind(reels: Reels) {
        this.reels = reels
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
                goToReelsFragment(navController)
            }
        }
    }
}