package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentSignUpBinding
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignUpBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        binding.topBackBtn.setOnClickListener(this)
        //todo: actionlar eklenince açılacak
        binding.signUpBtn.setOnClickListener(this)
//        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signInBtn.setOnClickListener(this)

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignUpFragmentDirections

        when (v) {
            binding.topBackBtn -> {
                action = directions.actionSignUpFragmentToLoginFragment()
            }
            binding.signUpBtn -> {
                action = directions.actionSignUpFragmentToHomeActivity()
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
}