package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initViews() {
        binding.loginBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = LoginFragmentDirections

        if (v == binding.loginBtn) {
            action = directions.actionLoginFragmentToSignInFragment()
        } else if (v == binding.signUpBtn) {
            action = directions.actionLoginFragmentToSignUpFragment()
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
        }
    }
}