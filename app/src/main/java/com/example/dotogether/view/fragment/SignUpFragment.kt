package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentSignUpBinding
import com.example.dotogether.util.Resource
import com.example.dotogether.util.ValidationFactory
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignUpBinding

    private var userName: String = ""
    private var email: String = ""
    private var password: String = ""
    private var passwordAgain: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)

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
        binding.usernameEditTxt.addTextChangedListener{ editTextChange(binding.usernameEditTxt) }
        binding.emailEditTxt.addTextChangedListener{ editTextChange(binding.emailEditTxt) }
        binding.passwordEditTxt.addTextChangedListener{ editTextChange(binding.passwordEditTxt) }
        binding.passwordAgainEditTxt.addTextChangedListener{ editTextChange(binding.passwordAgainEditTxt) }
        binding.topBackBtn.setOnClickListener(this)
        //todo: actionlar eklenince açılacak
        binding.signUpBtn.setOnClickListener(this)
//        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signInBtn.setOnClickListener(this)
    }

    private fun initObserve() {

    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignUpFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                action = directions.actionSignUpFragmentToLoginFragment()
            }
            binding.signUpBtn -> {
                validUserName()
                validEmail()
                validPassword()
                validPasswordAgain()
                if (validUserName() && validEmail() && validPassword() && validPasswordAgain()) {
                    action = directions.actionSignUpFragmentToHomeActivity()
                }
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
            binding.signInBtn -> {
                action = directions.actionSignUpFragmentToSignInFragment()
            }
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.signUpBtn) {
                activity?.finish()
            }
        }
    }

    private fun editTextChange(v: EditText) {
        when(v) {
            binding.usernameEditTxt -> {
                userName = v.text.toString()
                validUserName()
            }
            binding.emailEditTxt -> {
                email = v.text.toString()
                binding.emailEditLyt.error?.let {
                    validEmail()
                }
            }
            binding.passwordEditTxt -> {
                password = v.text.toString()
                binding.passwordEditLyt.error?.let {
                    validPassword()
                }
            }
            binding.passwordAgainEditTxt -> {
                passwordAgain = v.text.toString()
                binding.passwordAgainEditLyt.error?.let {
                    validPasswordAgain()
                }
            }
        }
    }

    private fun validUserName() : Boolean {
        return if (userName.isEmpty()) {
            binding.usernameEditLyt.error = "Wrong username"
            false
        } else {
            binding.usernameEditLyt.error = null
            true
        }
    }

    private fun validEmail() : Boolean {
        when (ValidationFactory.validMail(email)) {
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

    private fun validPassword() : Boolean {
        when (ValidationFactory.validPassword(password)) {
            is Resource.Success -> {
                binding.passwordEditLyt.error = null
                return true
            }
            is Resource.Error -> {
                binding.passwordEditLyt.error = "Wrong password"
                return false
            }
            else -> {}
        }
        return false
    }

    private fun validPasswordAgain() : Boolean {
        return if (passwordAgain.isEmpty() || password != passwordAgain || ValidationFactory.validPassword(passwordAgain) is Resource.Error) {
            binding.passwordAgainEditLyt.error = "Wrong password again"
            false
        } else {
            binding.passwordAgainEditLyt.error = null
            true
        }
    }
}