package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentPasswordEditBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordEditFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentPasswordEditBinding

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPasswordEditBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserve()
    }

    private fun initViews() {
        user = arguments?.getParcelable("user")
        binding.backBtn.setOnClickListener(this)
        binding.save.setOnClickListener(this)

        user?.let {
            RuntimeHelper.glideForPersonImage(requireContext()).load(it.img).into(binding.profileImage)
        }
    }

    private fun initObserve() {

    }

    override fun onClick(v: View?) {
        val navController = view?.findNavController()
        navController?.let {
            when (v) {
                binding.backBtn -> {
                    activity?.onBackPressed()
                }
                binding.save -> {
                    //todo update user info
                }
                else -> {}
            }
        }
    }
}