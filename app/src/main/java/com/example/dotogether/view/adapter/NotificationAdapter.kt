package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemNotificationBinding
import com.example.dotogether.model.Notification
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.NotificationHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener

class NotificationAdapter(private val notifications: ArrayList<Notification>, private val listener: HolderListener.NotificationHolderListener) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding.root, listener)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as NotificationHolder
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}