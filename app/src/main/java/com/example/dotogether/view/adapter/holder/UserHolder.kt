package com.example.dotogether.view.adapter.holder

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.UserRowBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.activity.OthersActivity

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
                val intent = Intent(binding.root.context, OthersActivity::class.java)
                intent.putExtra("view_type", 1)
                context.startActivity(intent)
            }
        }
    }
}