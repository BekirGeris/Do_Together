package com.example.dotogether.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.example.dotogether.databinding.FragmentLoginBinding
import com.example.dotogether.util.Resource
import com.example.dotogether.view.dialog.CustomProgressDialog
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment(), View.OnClickListener {

    private val TAG = "BEKBEK"

    private val viewModel: LoginViewModel by viewModels()
    lateinit var binding: FragmentLoginBinding

    lateinit var dialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)

        initViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initField()
        initObserve()
        return binding.root
    }

    private fun initField() {
        dialog = CustomProgressDialog(requireActivity())
    }

    private fun initViews() {
        binding.loginBtn.setOnClickListener(this)
        binding.signUpBtn.setOnClickListener(this)
    }

    private fun initObserve() {
        viewModel.autoLogin()
        viewModel.login.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeActivity()
                    Navigation.findNavController(requireView()).navigate(action)
                    requireActivity().finish()
                }
                is Resource.Error -> {
                    dialog.hide()
                    Log.d(TAG, "Error: ${it.message}")
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    dialog.shoe()
                }
            }
        }
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