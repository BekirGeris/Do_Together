package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel() {

    private val _login = MutableLiveData<Resource<LoginResponse>>()
    val login: MutableLiveData<Resource<LoginResponse>> = _login

    private val _register = MutableLiveData<Resource<RegisterResponse>>()
    val register: MutableLiveData<Resource<RegisterResponse>> = _register

    fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.login(loginRequest).collect {
                _login.value = it
            }
        }
    }

    fun register(name: String, username: String, email: String, password: String, passwordAgain: String) {
        val registerRequest = RegisterRequest(name, username, email, password, passwordAgain)

        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.regitser(registerRequest).collect {
                _register.value = it
            }
        }
    }
}