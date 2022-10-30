package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.R
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import java.util.ArrayList

class TargetHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemTargetBinding.bind(view)
    private val context = binding.root.context

    private var memberAdapter: MemberAdapter
    private val members = ArrayList<User>()

    init {
        initViews()
        for (i in 1..100) {
            members.add(User())
        }

        memberAdapter = MemberAdapter(members)
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
        binding.threeBtn.setOnClickListener(this)
        binding.userImage.setOnClickListener(this)
        binding.postUserName.setOnClickListener(this)
        binding.postTime.setOnClickListener(this)
        binding.postImage.setOnClickListener(this)
        binding.like.setOnClickListener(this)
        binding.join.setOnClickListener(this)
    }

    fun bind(target: Target) {
        binding.membersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.membersRv.adapter = memberAdapter
        binding.postImage.visibility = View.VISIBLE

        //todo: test bloc
        binding.postImage.setImageDrawable(when(target.url) {
            1 -> {
                ContextCompat.getDrawable(context, R.drawable.login_image)
            }
            2 -> {
                ContextCompat.getDrawable(context, R.drawable.ic_virus)
            }
            3 -> {
                ContextCompat.getDrawable(context, R.drawable.mechanic)
            }
            4 -> {
                ContextCompat.getDrawable(context, R.drawable.ic_watering_the_plants)
            }
            else -> {
                binding.postImage.visibility = View.GONE
                null
            }
        })
    }

    override fun onClick(v: View?) {
        val navController = v?.findNavController()
        when(v) {
            binding.holderView -> {

            }
            binding.threeBtn-> {

            }
            binding.postUserName, binding.userImage -> {
                 goToProfileFragment(navController)
            }
            binding.postTime -> {

            }
            binding.postImage -> {

            }
            binding.like -> {

            }
            binding.join -> {

            }
        }
    }
}