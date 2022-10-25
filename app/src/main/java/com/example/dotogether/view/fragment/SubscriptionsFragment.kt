package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentSubscriptionsBinding
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.viewmodel.LibraryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class SubscriptionsFragment : BaseFragment() {

    private val viewModel: LibraryViewModel by viewModels()
    private lateinit var binding: FragmentSubscriptionsBinding

    private lateinit var targetAdapter: TargetAdapter
    private val targets = ArrayList<Target>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSubscriptionsBinding.inflate(layoutInflater)

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
            targets.add(Target())
        }

        targetAdapter = TargetAdapter(targets)

        binding.targetRv.layoutManager = LinearLayoutManager(binding.root.context)
        binding.targetRv.adapter = targetAdapter

        println("bekbek ${viewModel.text.value}")
    }
}