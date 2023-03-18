package com.example.dotogether.view.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.view.adapter.holder.BaseHolder

abstract class BaseAdapter : RecyclerView.Adapter<BaseHolder>() {

    open fun deleteItem(position: Int) {}
}
