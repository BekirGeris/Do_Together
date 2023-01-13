package com.example.dotogether.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.dotogether.R
import com.example.dotogether.data.callback.LoginCallback
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.util.Constants
import com.example.dotogether.util.Resource
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.thread

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity(), LoginCallback {

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
                    this.loginSuccess(it)
                }
                is Resource.Error -> {
                    this.loginFailed(it)
                }
                is Resource.Loading -> {}
            }
        }

        thread {
            Thread.sleep(1000)
            viewModel.autoLogin()
        }
    }

    override fun loginSuccess(resource: Resource<LoginResponse>) {
        SharedPreferencesUtil.setString(this, Constants.TOKEN_KEY, resource.data?.token!!)
        val i = Intent(this@SplashActivity, HomeActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun loginFailed(resource: Resource<LoginResponse>) {
        if (resource.code == Constants.Status.NO_AUTO_LOGIN) {
            val i = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(i)
            finish()
        } else {
            showToast(resource.message)
        }
    }
}