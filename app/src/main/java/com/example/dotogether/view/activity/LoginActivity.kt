package com.example.dotogether.view.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.loginFragmentContainerView)

        this.onBackPressedDispatcher.addCallback {
            if (!navController.popBackStack()) {
                this@LoginActivity.finish()
            }
        }
    }
}