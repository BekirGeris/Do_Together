package com.example.dotogether.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.FragmentNotificationBinding
import com.example.dotogether.databinding.ItemNotificationBinding
import com.example.dotogether.model.Notification
import com.example.dotogether.util.Resource
import com.example.dotogether.view.adapter.NotificationAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.example.dotogether.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment(), View.OnClickListener, HolderListener.NotificationHolderListener {

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var binding: FragmentNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter
    private val notifications = ArrayList<Notification>()

    private var nextPage = "2"
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(!recyclerView.canScrollVertically(1)) {
                viewModel.getNextAllNotifications(nextPage)
                binding.notificationRv.removeOnScrollListener(this)
            }
        }
    }

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

        binding.swipeLyt.setOnRefreshListener {
            viewModel.getAllNotifications()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserve() {
        viewModel.notifications.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.swipeLyt.isRefreshing = false
                    it.data?.let { response ->
                        response.data?.let { list ->
                            binding.activityErrorView.visibility = if(list.isEmpty()) View.VISIBLE else View.GONE
                            notifications.clear()
                            notifications.addAll(list)
                            notificationAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                    dialog.hide()
                    viewModel.notificationsReadAll().observe(viewLifecycleOwner) { resource ->
                        when(resource) {
                            is Resource.Error -> {
                                showToast(resource.message)
                            }
                            else -> {}
                        }
                    }
                }
                is Resource.Error -> {
                    binding.swipeLyt.isRefreshing = false
                    binding.activityErrorView.visibility = View.VISIBLE
                    dialog.hide()
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    if (!dialog.dialog.isShowing && !binding.swipeLyt.isRefreshing) {
                        dialog.show()
                    }
                }
                else -> {}
            }
        }
        viewModel.getAllNotifications()
        viewModel.nextNotifications.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    binding.nextProgressBar.visibility = View.GONE
                    it.data?.let { response ->
                        response.data?.let { list ->
                            notifications.addAll(list)
                            notificationAdapter.notifyDataSetChanged()
                        }
                        response.next_page_url?.let { next_page_url ->
                            nextPage = next_page_url.last().toString()
                            setRecyclerViewScrollListener()
                        }
                    }
                }
                is Resource.Error -> {
                    binding.nextProgressBar.visibility = View.GONE
                    showToast(it.message)
                }
                is Resource.Loading -> {
                    binding.nextProgressBar.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            else -> {}
        }
    }

    private fun setRecyclerViewScrollListener() {
        binding.notificationRv.addOnScrollListener(scrollListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClickNotification(binding: ItemNotificationBinding, notification: Notification) {
        notifications.map { if (it == notification) it.is_read = true }
        notificationAdapter.notifyDataSetChanged()
    }
}