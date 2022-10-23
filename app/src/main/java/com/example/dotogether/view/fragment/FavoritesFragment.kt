package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentFavoritesBinding
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.TargetAdapter
import java.util.ArrayList

class FavoritesFragment : BaseFragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private lateinit var targetAdapter: TargetAdapter
    private val targets = ArrayList<Target>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoritesBinding.inflate(layoutInflater)

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
    }
}