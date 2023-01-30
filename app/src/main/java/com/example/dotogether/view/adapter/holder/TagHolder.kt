package com.example.dotogether.view.adapter.holder

import android.annotation.SuppressLint
import android.view.View
import com.example.dotogether.databinding.ItemTagBinding
import com.example.dotogether.model.Tag
import com.example.dotogether.view.adapter.holderListener.HolderListener

class TagHolder(view: View, private val tagHolderListener: HolderListener.TagHolderListener) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemTagBinding.bind(view)
    private val context = binding.root.context

    private lateinit var tag: Tag

    @SuppressLint("SetTextI18n")
    fun bind(tag: Tag) {
        this.tag = tag
        binding.tagName.setOnClickListener(this)
        binding.tagName.text = "# ${tag.name}"
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tagName -> {
                tagHolderListener.addTag(tag.name)
            }
        }
    }
}