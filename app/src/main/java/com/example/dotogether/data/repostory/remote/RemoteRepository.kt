package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.Target
import com.example.dotogether.model.request.CreateTargetRequest
import com.example.dotogether.model.request.LoginRequest
import com.example.dotogether.model.request.RegisterRequest
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.model.response.Response
import com.example.dotogether.model.response.LoginResponse
import com.example.dotogether.model.response.RegisterResponse
import retrofit2.http.*

interface RemoteRepository {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("activity/create")
    suspend fun createTarget(@Body createTargetRequest: CreateTargetRequest) : Response<Target>

    @GET("activity")
    suspend fun getAllTargets() : Response<GetAllTargetsResponse>

    @GET("activity?page")
    suspend fun getNextAllTargets(@Query("page") pageNo: String) : Response<GetAllTargetsResponse>

    @GET("activity/joined/active")
    suspend fun getMyJoinedTargets() : Response<GetAllTargetsResponse>

    @GET("activity/likes")
    suspend fun getMyLikeTargets() : Response<GetAllTargetsResponse>

    @GET("activity/my/done")
    suspend fun getMyDoneTargets() : Response<GetAllTargetsResponse>

    @GET("activity/my")
    suspend fun getMyTargets() : Response<GetAllTargetsResponse>

    @GET("activity/join/{targetId}")
    suspend fun joinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/like/{targetId}")
    suspend fun likeTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/leave/{targetId}")
    suspend fun unJoinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/unlike/{targetId}")
    suspend fun unLikeTarget(@Path("targetId") targetId: Int) : Response<Target>
}