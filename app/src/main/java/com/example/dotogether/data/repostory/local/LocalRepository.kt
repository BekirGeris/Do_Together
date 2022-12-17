package com.example.dotogether.data.repostory.local

import retrofit2.http.GET
import retrofit2.http.Query

interface LocalRepository {

    @GET("prices")
    suspend fun localtest(@Query("key") key: String)
}