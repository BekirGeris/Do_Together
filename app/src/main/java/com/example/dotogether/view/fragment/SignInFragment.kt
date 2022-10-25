package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentSignInBinding
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSignInBinding.inflate(layoutInflater)

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
        //todo: acitonlar eklenince açılacak
        binding.loginBtn.setOnClickListener(this)
//        binding.signUpBtn.setOnClickListener(this)
//        binding.forgetPasswordBtn.setOnClickListener(this)
//        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)

        println("bekbek ${viewModel.text.value}")
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignInFragmentDirections

        if (v == binding.topBackBtn) {
            action = directions.actionSignInFragmentToLoginFragment()
        } else if (v == binding.loginBtn) {
            action = directions.actionSignInFragmentToHomeActivity()
        } else if (v == binding.forgetPasswordBtn) {
            //todo: şifre yenileme ekranına gidilecek
        } else if (v == binding.googleBtn) {
            //todo: GOOGLE ile giriş
        } else if (v == binding.facebookBtn) {
            //todo: FACEBOOK ile giriş
        } else if (v == binding.twitterBtn) {
            //todo: TWITTER ile giriş
        } else if (v == binding.signUpBtn) {
            action = directions.actionSignInFragmentToSignUpFragment()
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.loginBtn) {
                activity?.finish()
            }
        }
    }
}