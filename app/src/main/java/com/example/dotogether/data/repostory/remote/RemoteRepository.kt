package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.model.response.Response
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteRepository {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @GET("activity")
    suspend fun getAllTargets() : Response<GetAllTargetsResponse>

    @GET("activity/joined/active")
    suspend fun getMyJoinedTargets() : Response<GetAllTargetsResponse>

    @GET("activity/likes")
    suspend fun getMyLikeTargets() : Response<GetAllTargetsResponse>

    @GET("activity/my/done")
    suspend fun getMyDoneTargets() : Response<GetAllTargetsResponse>

    @GET("activity/my")
    suspend fun getMyTargets() : Response<GetAllTargetsResponse>
}