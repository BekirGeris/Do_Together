package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.view.adapter.ReelsAdapter

class ReelsTopHolder(val view: View) : BaseHolder(view), View.OnClickListener {
    private val binding = ItemReelsTopBinding.bind(view)
    private val context = binding.root.context

    private var reelsAdapter: ReelsAdapter
    private var reelsList = java.util.ArrayList<Reels>()

    init {
        initViews()
        for (i in 1..100) {
            reelsList.add(Reels())
        }

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