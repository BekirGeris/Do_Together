package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dotogether.databinding.FragmentNotificationBinding
import com.example.dotogether.databinding.ItemNotificationBinding
import com.example.dotogether.model.Notification
import com.example.dotogether.view.adapter.NotificationAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment(), View.OnClickListener, HolderListener.NotificationHolderListener {

    private lateinit var binding: FragmentNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter
    private val notifications = ArrayList<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNotificationBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initViews() {
        binding.backBtn.setOnClickListener(this)

        notificationAdapter = NotificationAdapter(notifications, this)
        binding.notificationRv.adapter = notificationAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        for (i in 1..100) {
            val notification = Notification()
            notification.isLooked = i % 3 == 0
            notifications.add(notification)
        }
        notificationAdapter.notifyDataSetChanged()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                activity?.onBackPressed()
            }
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickNotification(binding: ItemNotificationBinding, notification: Notification) {
        notifications.map { if (it == notification) it.isLooked = true }
        notificationAdapter.notifyDataSetChanged()
    }
}