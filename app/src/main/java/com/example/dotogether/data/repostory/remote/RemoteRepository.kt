package com.example.dotogether.data.repostory.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteRepository {

    @GET("prices")
    suspend fun remotetest(@Query("key") key: String)
}