package com.example.dotogether.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.dotogether.R
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initObserve()
    }

    private fun initObserve() {
        viewModel.login.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val i = Intent(this@SplashActivity, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                }
                is Resource.Error -> {
                    if (it.code == Constants.Status.NO_AUTO_LOGIN) {
                        val i = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {}
            }
            setTheme(R.style.Theme_DoTogether)
        }

        thread {
            if (isDarkThemeOn()) {
                Thread.sleep(1000)
            }
            viewModel.autoLogin()
        }
    }

    private fun isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}