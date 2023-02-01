package com.example.dotogether.view.adapter.holder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.view.adapter.ReelsAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener

class ReelsTopHolder(view: View, val reelsList: ArrayList<User>, val reelsTopHolderListener: HolderListener.ReelsTopHolderListener, reelsHolderListener: HolderListener.ReelsHolderListener) : BaseHolder(view), View.OnClickListener {
    val binding = ItemReelsTopBinding.bind(view)
    val context = binding.root.context

    var reelsAdapter: ReelsAdapter

    init {
        reelsAdapter = ReelsAdapter(reelsList, reelsHolderListener)
        binding.reelsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.reelsRv.adapter = reelsAdapter

        initViews()
    }

    private fun initViews() {
        binding.addReelsBtn.setOnClickListener(this)

        reelsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.addReelsBtn.visibility = if (reelsList.isEmpty()) View.VISIBLE else View.GONE
            }
        })
    }

    fun bind() {

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.addReelsBtn -> {
                reelsTopHolderListener.addReels()
            }
        }
    }
}