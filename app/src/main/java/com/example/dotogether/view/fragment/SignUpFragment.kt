package com.example.dotogether.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment(), View.OnClickListener {

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
//        binding.signUpBtn.setOnClickListener(this)
//        binding.googleBtn.setOnClickListener(this)
//        binding.facebookBtn.setOnClickListener(this)
//        binding.twitterBtn.setOnClickListener(this)
        binding.signInBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var action: NavDirections? = null
        val directions = SignUpFragmentDirections

        if (v == binding.topBackBtn) {
            action = directions.actionSignUpFragmentToLoginFragment()
        } else if (v == binding.signUpBtn) {
            action = directions.actionSignUpFragmentToHomeActivity()
        } else if (v == binding.googleBtn) {
            //todo: GOOGLE ile giriş
        } else if (v == binding.facebookBtn) {
            //todo: FACEBOOK ile giriş
        } else if (v == binding.twitterBtn) {
            //todo: TWITTER ile giriş
        } else if (v == binding.signInBtn) {
            action = directions.actionSignUpFragmentToSignInFragment()
        }

        if (v != null && action != null) {
            Navigation.findNavController(v).navigate(action)
            if (v == binding.signUpBtn) {
                activity?.finish()
            }
        }
    }
}