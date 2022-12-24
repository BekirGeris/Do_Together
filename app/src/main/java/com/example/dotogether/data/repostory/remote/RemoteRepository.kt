package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.model.response.Response
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteRepository {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("activity")
    suspend fun getAllTargets() : Response<GetAllTargetsResponse>

}