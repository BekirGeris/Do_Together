package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.UserRowBinding
import com.example.dotogether.model.User

class UserHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = UserRowBinding.bind(view)

    fun bind(user: User) {

    }
}