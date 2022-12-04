package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentSignInBinding
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignInBinding

    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignInBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initObserve()
        return binding.root
    }

    private fun initViews() {
        binding.emailEditTxt.addTextChangedListener { editTextChange(binding.emailEditTxt) }
        binding.passwordEditTxt.addTextChangedListener { editTextChange(binding.passwordEditTxt) }
        binding.topBackBtn.setOnClickListener(this)
        //todo: acitonlar eklenince açılacak
        binding.loginBtn.setOnClickListener(this)
        binding.forgetPasswordBtn.setOnClickListener(this)
//        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    private fun initObserve() {

    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignInFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                action = directions.actionSignInFragmentToLoginFragment()
            }
            binding.loginBtn -> {
                if (validEmail() && validPassword()) {
                    action = directions.actionSignInFragmentToHomeActivity()
                }
            }
            binding.forgetPasswordBtn -> {
                if (validEmail()) {
                    Toast.makeText(requireContext(), "Email Sent", Toast.LENGTH_LONG).show()
                }
                binding.passwordErrorTxt.visibility = View.GONE
            }
            binding.googleBtn -> {
                //todo: GOOGLE ile giriş
            }
            binding.facebookBtn -> {
                //todo: FACEBOOK ile giriş
            }
            binding.twitterBtn -> {
                //todo: TWITTER ile giriş
            }
            binding.signUpBtn -> {
                action = directions.actionSignInFragmentToSignUpFragment()
            }
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.loginBtn) {
                activity?.finish()
            }
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.emailEditTxt -> {
                email = v.text.toString()
                validEmail()
            }
            binding.passwordEditTxt -> {
                password = v.text.toString()
                validPassword()
            }
        }
    }

    private fun validEmail() : Boolean {
        when (ValidationFactory.validMail(email)) {
            is Resource.Success -> {
                binding.emailErrorTxt.visibility = View.GONE
                return true
            }
            is Resource.Error -> {
                binding.emailErrorTxt.visibility = View.VISIBLE
                return false
            }
            else -> {}
        }
        return false
    }

    private fun validPassword() : Boolean {
        when (ValidationFactory.validPassword(password)) {
            is Resource.Success -> {
                binding.passwordErrorTxt.visibility = View.GONE
                return true
            }
            is Resource.Error -> {
                binding.passwordErrorTxt.visibility = View.VISIBLE
                return false
            }
            else -> {}
        }
        return false
    }
}