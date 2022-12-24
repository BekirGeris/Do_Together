package com.example.dotogether.data.callback

import com.example.dotogether.model.response.RegisterResponse
import com.example.dotogether.util.Resource

interface RegisterCallback {
    fun registerSuccess(resource: Resource<RegisterResponse>)
    fun registerFailed(resource: Resource<RegisterResponse>)
}