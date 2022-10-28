package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.ReelsColumnBinding
import com.example.dotogether.model.Reels

class ReelsHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var binding: ReelsColumnBinding = ReelsColumnBinding.bind(view)
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
        when(v) {
            binding.userImage -> {

            }
        }
    }
}