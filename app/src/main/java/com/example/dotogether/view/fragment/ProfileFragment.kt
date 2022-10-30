package com.example.dotogether.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentProfileBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.ProfileTargetAdapter
import com.example.dotogether.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    private lateinit var targetAdapter: ProfileTargetAdapter
    private val targets = ArrayList<Target>()
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        for (i in 1..1000) {
            targets.add(Target())
        }

        user = User()

        targetAdapter = ProfileTargetAdapter(targets, User())

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.targetRv.adapter = targetAdapter

        println("bekbek ${viewModel.text.value}")
    }
}