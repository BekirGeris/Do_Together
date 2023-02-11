package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.dotogether.databinding.FragmentForgetPasswordVerifyBinding
import com.example.dotogether.model.request.ForgetPasswordVerifyRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordFragmentVerify : BaseFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentForgetPasswordVerifyBinding

    private lateinit var email: String
    private lateinit var verifyCode: String
    private lateinit var newPassword: String
    private lateinit var newAgainPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentForgetPasswordVerifyBinding.inflate(layoutInflater)

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
        email = arguments?.getString("email")!!
        binding.backBtn.setOnClickListener(this)
        binding.save.setOnClickListener(this)

        binding.verifyCodeTxt.addTextChangedListener{ editTextChange(binding.verifyCodeTxt) }
        binding.newPasswordEditTxt.addTextChangedListener{ editTextChange(binding.newPasswordEditTxt) }
        binding.newPasswordAgainEditTxt.addTextChangedListener{ editTextChange(binding.newPasswordAgainEditTxt) }
    }

    private fun initObserve() {
        viewModel.forgetPasswordVerify.observe(viewLifecycleOwner) { resource ->
            when(resource) {
                is Resource.Success -> {
                    dialog.hide()
                    showToast(resource.message)
                    view?.findNavController()?.navigate(ForgetPasswordFragmentVerifyDirections.actionForgetPasswordFragmentToSignInFragment())
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
                view?.findNavController()?.navigate(ForgetPasswordFragmentVerifyDirections.actionForgetPasswordFragmentToSignInFragment())
            }
            binding.save -> {
                if (validVerifyCode() && validNewPassword() && validNewPasswordAgain()) {
                    viewModel.forgotPasswordVerify(ForgetPasswordVerifyRequest(email, verifyCode, newPassword, newAgainPassword))
                }
            }
            else -> {}
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.verifyCodeTxt -> {
                verifyCode = v.text.toString()
                binding.verifyCodeLyt.error?.let {
                    validVerifyCode()
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


    private fun validVerifyCode() : Boolean {
        ValidationFactory.validVerifyCode(verifyCode).let {
            when (it) {
                is Resource.Success -> {
                    binding.verifyCodeLyt.error = null
                    return true
                }
                is Resource.Error -> {
                    binding.verifyCodeLyt.error = it.message
                    return false
                }
                else -> {}
            }
        }
        return false
    }


    private fun validNewPassword() : Boolean {
        ValidationFactory.validPassword(newPassword).let {
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
            binding.newPasswordAgainEditLyt.error = "Passwords are not the same"
            false
        } else {
            binding.newPasswordAgainEditLyt.error = null
            true
        }
    }
}