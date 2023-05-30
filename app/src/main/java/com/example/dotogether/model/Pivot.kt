package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

data class Pivot(
    @SerializedName("following_id")
    val following_id: Int,
    @SerializedName("user_id")
    val user_id: Int
)