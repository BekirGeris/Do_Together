package com.example.dotogether.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.databinding.FragmentHomeBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.ReelsAdapter
import com.example.dotogether.view.adapter.TargetAdapter
import java.util.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var reelsAdapter: ReelsAdapter
    private var reelsList = ArrayList<Reels>()
    private lateinit var targetAdapter: TargetAdapter
    private var targetList = ArrayList<Target>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initViews()

        for (i in 1..10000) {
            reelsList.add(Reels())
            targetList.add(Target())
        }

        reelsAdapter = ReelsAdapter(reelsList)
        binding.reelsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.reelsRv.adapter = reelsAdapter

        targetAdapter = TargetAdapter(targetList)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = targetAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    fun initViews() {

    }
}