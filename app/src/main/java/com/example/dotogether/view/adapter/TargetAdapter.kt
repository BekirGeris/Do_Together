package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.TargetRowBinding
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.holder.TargetHolder
import java.util.ArrayList

class TargetAdapter(private val targetList: ArrayList<Target>) : RecyclerView.Adapter<TargetHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TargetHolder {
        val binding = TargetRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TargetHolder(binding.root)
    }

    override fun onBindViewHolder(holder: TargetHolder, position: Int) {
        holder.bind(targetList[position])
    }

    override fun getItemCount(): Int {
        return targetList.size
    }
}