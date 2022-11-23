package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentFollowsBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.UserAdapter
import java.util.ArrayList

class FollowsFragment : BaseFragment() {

    private lateinit var binding: FragmentFollowsBinding

    private lateinit var userAdapter: UserAdapter

    private val users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFollowsBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        for (i in 1..100) {
            users.add(User())
        }

        userAdapter = UserAdapter(users)
        binding.followRv.layoutManager = LinearLayoutManager(context)
        binding.followRv.adapter = userAdapter
    }
}