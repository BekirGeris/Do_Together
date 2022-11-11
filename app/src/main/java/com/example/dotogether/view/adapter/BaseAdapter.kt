package com.example.dotogether.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.callback.HolderCallback

abstract class BaseAdapter : RecyclerView.Adapter<BaseHolder>() {

    private lateinit var onClickListener : HolderCallback

    fun setOnClickListener(onClickListener: HolderCallback) {
        this.onClickListener = onClickListener
    }

    fun getOnClickListener() : HolderCallback {
        return onClickListener
    }
}
