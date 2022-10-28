package com.example.dotogether.view.adapter.holder

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.MemberColumnBinding
import com.example.dotogether.model.User

class MemberHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val binding = MemberColumnBinding.bind(view)
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
        when(v) {
            binding.userImage -> {

            }
        }
    }
}