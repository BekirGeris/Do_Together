package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.UserRowBinding
import com.example.dotogether.model.User

class UserHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val binding = UserRowBinding.bind(view)
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
        when(v) {
            binding.holderView -> {

            }
        }
    }
}