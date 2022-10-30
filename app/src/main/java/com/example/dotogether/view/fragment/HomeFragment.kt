package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.databinding.FragmentHomeBinding
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.HomeTargetAdapter
import com.example.dotogether.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class HomeFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    private lateinit var homeTargetAdapter: HomeTargetAdapter
    private var targetList = ArrayList<Target>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    fun initViews() {
        binding.cameraBtn.setOnClickListener(this)
        binding.messageBtn.setOnClickListener(this)

        navController = findNavController()

        for (i in 1..10000) {
            targetList.add(Target())
        }

        homeTargetAdapter = HomeTargetAdapter(targetList)
        binding.targetRv.layoutManager = LinearLayoutManager(context)
        binding.targetRv.adapter = homeTargetAdapter

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.cameraBtn -> {

            }
            binding.messageBtn -> {
                navController.navigate(HomeNavDirections.actionGlobalOthersActivity(viewType = 2))
            }
        }
    }
}