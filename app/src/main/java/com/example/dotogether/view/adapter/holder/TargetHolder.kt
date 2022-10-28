package com.example.dotogether.view.adapter.holder

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.R
import com.example.dotogether.databinding.TargetRowBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import java.util.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

class TargetHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private val binding = TargetRowBinding.bind(view)
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
        binding.postUserName.setOnClickListener(this)
        binding.postTime.setOnClickListener(this)
        binding.postImage.setOnClickListener(this)
        binding.like.setOnClickListener(this)
        binding.join.setOnClickListener(this)
    }

    fun bind(target: Target) {
        binding.membersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.membersRv.adapter = memberAdapter

        //todo: test bloc
        binding.postImage.setImageDrawable(when(Random.nextInt(IntRange(1, 5))) {
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
        when(v) {
            binding.holderView -> {

            }
            binding.threeBtn-> {

            }
            binding.postUserName -> {

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