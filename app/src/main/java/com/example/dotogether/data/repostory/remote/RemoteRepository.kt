package com.example.dotogether.data.repostory.remote

import com.example.dotogether.model.*
import com.example.dotogether.model.Tag
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

    @POST("user/update-password")
    suspend fun updatePassword(@Body updatePasswordRequest: UpdatePasswordRequest) : Response<User>

    @POST("forgot-password")
    suspend fun forgotPassword(@Body forgetPasswordRequest: ForgetPasswordRequest) : Response<String>

    @POST("verify-otp")
    suspend fun forgotPasswordVerify(@Body forgetPasswordVerifyRequest: ForgetPasswordVerifyRequest) : Response<String>

    @POST("chat/create")
    suspend fun newChat(@Body newChatRequest: NewChatRequest) : Response<NewChatResponse>

    @POST("chat/send")
    suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest) : Response<SendMessageResponse>

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

    @GET("activity/my/{userId}")
    suspend fun getTargetsWithUserId(@Path("userId") userId: Int) : Response<Page<Target>>

    @GET("activity/my/{userId}?page")
    suspend fun getNextTargetsWithUserId(@Path("userId") userId: Int, @Query("page") pageNo: String) : Response<Page<Target>>

    @GET("activity/join/{targetId}")
    suspend fun joinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/like/{targetId}")
    suspend fun likeTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/leave/{targetId}")
    suspend fun unJoinTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/unlike/{targetId}")
    suspend fun unLikeTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/get/{targetId}")
    suspend fun getTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("activity/delete/{targetId}")
    suspend fun deleteTarget(@Path("targetId") targetId: Int) : Response<Target>

    @GET("user/get/{userId}")
    suspend fun getUser(@Path("userId") userId: Int) : Response<User>

    @GET("user/followers/{userId}")
    suspend fun getFollowers(@Path("userId") userId: Int) : Response<Page<User>>

    @GET("user/followings/{userId}")
    suspend fun getFollowings(@Path("userId") userId: Int) : Response<Page<User>>

    @GET("user/followers/{userId}?page")
    suspend fun getNextFollowers(@Path("userId") userId: Int, @Query("page") pageNo: String) : Response<Page<User>>

    @GET("user/followings/{userId}?page")
    suspend fun getNextFollowings(@Path("userId") userId: Int, @Query("page") pageNo: String) : Response<Page<User>>

    @GET("user/follow/{userId}")
    suspend fun follow(@Path("userId") userId: Int) : Response<User>

    @GET("user/unfollow/{userId}")
    suspend fun unFollow(@Path("userId") userId: Int) : Response<User>

    @GET("user/followings-statuses")
    suspend fun getFollowingsReels() : Response<ArrayList<User>>

    @GET("user/status/remove/{reelsId}")
    suspend fun removeReels(@Path("reelsId") reelsId: Int) : Response<Reels>

    @POST("search/user")
    suspend fun searchUser(@Body searchRequest: SearchRequest) : Response<ArrayList<User>>

    @POST("search/activity")
    suspend fun searchTarget(@Body searchRequest: SearchRequest) : Response<ArrayList<Target>>

    @POST("search/tag")
    suspend fun searchTag(@Body searchRequest: SearchRequest) : Response<ArrayList<Tag>>

    @GET("chat/my")
    suspend fun myChats() : Response<List<MyChatsResponse>>

    @POST("chat/my")
    suspend fun searchMyChats(@Body searchRequest: SearchRequest) : Response<List<MyChatsResponse>>

    @POST("user/followings/{userId}")
    suspend fun searchFollowings(@Body searchRequest: SearchRequest, @Path("userId") userId: Int) : Response<ArrayList<User>>

    @POST("user/followers/{userId}")
    suspend fun searchFollowers(@Body searchRequest: SearchRequest, @Path("userId") userId: Int) : Response<ArrayList<User>>

    @GET("chat/get/{chatId}")
    suspend fun getChat(@Path("chatId") chatId: String) : Response<MyChatsResponse>
}