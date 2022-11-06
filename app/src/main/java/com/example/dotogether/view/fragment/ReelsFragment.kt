package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentReelsBinding
import com.example.dotogether.viewmodel.ReelsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReelsFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ReelsViewModel by viewModels()
    private lateinit var binding: FragmentReelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReelsBinding.inflate(layoutInflater)

        initViews();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        println("bekbek ${viewModel.text}")
        binding.backBtn.setOnClickListener(this)
        binding.reelsUserName.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val navController = v?.findNavController()
       navController?.let {
           when(v) {
               binding.backBtn -> {
                   if (!navController.popBackStack()) {
                       activity?.finish()
                   }
               }
               binding.reelsUserName -> {
                    navController.navigate(ReelsFragmentDirections.actionReelsFragmentToProfileFragment())
               }
           }
       }
    }
}