package com.example.dotogether.view.adapter.holder

import android.view.View
import com.example.dotogether.databinding.ItemReelsBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.adapter.holderListener.HolderListener

class ReelsHolder(view: View, private val reelsHolderListener: HolderListener.ReelsHolderListener) : BaseHolder(view), View.OnClickListener {

    private var binding = ItemReelsBinding.bind(view)
    private val context = binding.root.context
    private lateinit var user: User

    init {
        initViews()
    }

    private fun initViews() {
        binding.userImage.setOnClickListener(this)
    }

    fun bind(user: User) {
        this.user = user

        binding.userNameTxt.text = user.username
        RuntimeHelper.glideForPersonImage(context).load(user.img).into(binding.userImage)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.userImage -> {
                reelsHolderListener.onClickReels(binding, user)
            }
        }
    }
}