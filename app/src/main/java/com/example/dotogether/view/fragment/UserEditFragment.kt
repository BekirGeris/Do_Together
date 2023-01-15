package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentUserEditBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.ProfileViewModel

class UserEditFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentUserEditBinding

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUserEditBinding.inflate(layoutInflater)

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
            binding.nameEditTxt.setText(it.name)
            binding.usernameEditTxt.setText(it.username)
            binding.emailEditTxt.setText(it.email)
            binding.descriptionEditTxt.setText(it.description)
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