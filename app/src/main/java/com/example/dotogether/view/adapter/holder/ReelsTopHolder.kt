package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.view.adapter.ReelsAdapter

class ReelsTopHolder(view: View, reelsList: ArrayList<Reels>) : BaseHolder(view), View.OnClickListener {
    val binding = ItemReelsTopBinding.bind(view)
    val context = binding.root.context

    var reelsAdapter: ReelsAdapter

    init {
        initViews()

        reelsAdapter = ReelsAdapter(reelsList)
        binding.reelsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.reelsRv.adapter = reelsAdapter
    }

    private fun initViews() {

    }

    fun bind() {

    }

    override fun onClick(v: View?) {

    }
}