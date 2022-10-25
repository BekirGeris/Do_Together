package com.example.dotogether.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotogether.databinding.FragmentShareBinding
import com.example.dotogether.viewmodel.ShareViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareFragment : Fragment() {

    private val viewModel: ShareViewModel by viewModels()
    private lateinit var binding: FragmentShareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentShareBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    fun initViews() {
        println("bekbek ${viewModel.text.value}")
    }
}