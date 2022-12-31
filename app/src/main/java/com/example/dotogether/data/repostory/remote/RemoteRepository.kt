package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.*
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.*
import com.example.dotogether.model.response.*
import retrofit2.http.*

interface RemoteRepository {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<RegisterResponse>

    @POST("update")
    suspend fun updateUser(@Body updateUserRequest: UpdateUserRequest) : Response<User>

    @POST("user/status")
    suspend fun createReels(@Body createReelsRequest: CreateReelsRequest) : Response<Reels>

    @POST("activity/create")
    suspend fun createTarget(@Body createTargetRequest: CreateTargetRequest) : Response<Target>

    @GET("activity")
    suspend fun getAllTargets() : Response<Page<Target>>

    @GET("activity?page")
    suspend fun getNextAllTargets(@Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/joined/active")
    suspend fun getMyJoinedTargets() : Response<Page<Target>>

    @GET("activity/joined/active?page")
    suspend fun getNextMyJoinedTargets(@Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/likes")
    suspend fun getMyLikeTargets() : Response<Page<Target>>

    @GET("activity/likes?page")
    suspend fun getNextMyLikeTargets(@Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/my/done")
    suspend fun getMyDoneTargets() : Response<Page<Target>>

    @GET("activity/my/done?page")
    suspend fun getNextMyDoneTargets(@Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/my")
    suspend fun getMyTargets() : Response<Page<Target>>

    @GET("activity/my?page")
    suspend fun getNextMyTargets(@Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/join/{targetId}")
    suspend fun joinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/like/{targetId}")
    suspend fun likeTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/leave/{targetId}")
    suspend fun unJoinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/unlike/{targetId}")
    suspend fun unLikeTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("user/followers")
    suspend fun getFollowers() : Response<Page<Connection>>

    @GET("user/followings")
    suspend fun getFollowings() : Response<Page<Connection>>
}