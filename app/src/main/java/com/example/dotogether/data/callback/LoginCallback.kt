package com.example.dotogether.data.callback

import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.util.Resource

interface LoginCallback {
    fun loginSuccess(resource: Resource<LoginResponse>)
    fun loginFailed(resource: Resource<LoginResponse>)
}