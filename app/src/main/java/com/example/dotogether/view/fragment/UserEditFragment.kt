package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dotogether.databinding.FragmentUserEditBinding
import com.example.dotogether.model.User
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        viewModel.updateUser.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(resource.message)
                    activity?.onBackPressed()
                }
                is Resource.Error -> {
                    dialog.hide()
                    showToast(resource.message)
                }
                is Resource.Loading -> {
                    dialog.show()
                }
                else -> {}
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.backBtn -> {
                activity?.onBackPressed()
            }
            binding.save -> {
                updateUser()
            }
            else -> {}
        }
    }

    private fun updateUser() {
        user?.let {
            it.name = binding.nameEditTxt.text.toString()
            it.username = binding.usernameEditTxt.text.toString()
            it.email = binding.emailEditTxt.text.toString()
            it.description = binding.descriptionEditTxt.text.toString()

            if (validName() && validUserName() && validEmail()) {
                val updateUserRequest = UpdateUserRequest(
                    name = it.name,
                    username = it.username,
                    email = it.email,
                    description = it.description
                )
                viewModel.updateUser(updateUserRequest)
            }
        }
    }

    private fun validName() : Boolean {
        return if (user?.name?.isEmpty() == true) {
            binding.nameEditLyt.error = "Wrong name"
            false
        } else {
            binding.nameEditLyt.error = null
            true
        }
    }

    private fun validUserName() : Boolean {
        return if (user?.username?.isEmpty() == true) {
            binding.usernameEditLyt.error = "Wrong username"
            false
        } else {
            binding.usernameEditLyt.error = null
            true
        }
    }

    private fun validEmail() : Boolean {
        when (ValidationFactory.validMail(user?.email)) {
            is Resource.Success -> {
                binding.emailEditLyt.error = null
                return true
            }
            is Resource.Error -> {
                binding.emailEditLyt.error = "Wrong email"
                return false
            }
            else -> {}
        }
        return false
    }
}