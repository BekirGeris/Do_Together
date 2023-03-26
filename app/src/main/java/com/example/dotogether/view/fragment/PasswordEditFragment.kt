package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.example.dotogether.R
import com.example.dotogether.databinding.FragmentPasswordEditBinding
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.ProfileViewModel
import com.example.dotogether.model.request.UpdatePasswordRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordEditFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentPasswordEditBinding

    private var user: User? = null

    private lateinit var oldPassword: String
    private lateinit var newPassword: String
    private lateinit var newAgainPassword: String

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

        binding.oldPasswordEditTxt.addTextChangedListener{ editTextChange(binding.oldPasswordEditTxt) }
        binding.newPasswordEditTxt.addTextChangedListener{ editTextChange(binding.newPasswordEditTxt) }
        binding.newPasswordAgainEditTxt.addTextChangedListener{ editTextChange(binding.newPasswordAgainEditTxt) }

        user?.let {
            RuntimeHelper.glideForPersonImage(requireContext()).load(it.img).into(binding.profileImage)
        }
    }

    private fun initObserve() {
        viewModel.updatePassword.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(resource.message)
                    activity?.onBackPressedDispatcher?.onBackPressed()
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
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            binding.save -> {
                if (validOldPassword() && validNewPassword() && validNewPasswordAgain()) {
                    viewModel.updatePassword(UpdatePasswordRequest(oldPassword, newPassword, newAgainPassword))
                }
            }
            else -> {}
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.oldPasswordEditTxt -> {
                oldPassword = v.text.toString()
                binding.oldPasswordEditLyt.error?.let {
                    validOldPassword()
                }
            }
            binding.newPasswordEditTxt -> {
                newPassword = v.text.toString()
                binding.newPasswordEditLyt.error?.let {
                    validNewPassword()
                }
            }
            binding.newPasswordAgainEditTxt -> {
                newAgainPassword = v.text.toString()
                binding.newPasswordAgainEditLyt.error?.let {
                    validNewPasswordAgain()
                }
            }
        }
    }


    private fun validOldPassword() : Boolean {
        ValidationFactory.validPassword(oldPassword, requireContext()).let {
            when (it) {
                is Resource.Success -> {
                    binding.oldPasswordEditLyt.error = null
                    return true
                }
                is Resource.Error -> {
                    binding.oldPasswordEditTxt.error = it.message
                    return false
                }
                else -> {}
            }
        }
        return false
    }


    private fun validNewPassword() : Boolean {
        ValidationFactory.validPassword(newPassword, requireContext()).let {
            when (it) {
                is Resource.Success -> {
                    binding.newPasswordEditLyt.error = null
                    return true
                }
                is Resource.Error -> {
                    binding.newPasswordEditLyt.error = it.message
                    return false
                }
                else -> {}
            }
        }
        return false
    }

    private fun validNewPasswordAgain() : Boolean {
        return if (newAgainPassword.isEmpty() || newPassword != newAgainPassword) {
            binding.newPasswordAgainEditLyt.error = getString(R.string.new_password_error_message)
            false
        } else {
            binding.newPasswordAgainEditLyt.error = null
            true
        }
    }
}