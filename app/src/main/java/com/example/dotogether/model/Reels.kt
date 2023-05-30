package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Reels(
    @SerializedName("created_at")
    var created_at: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("img")
    var img: String,
    @SerializedName("updated_at")
    var updated_at: String,
    @SerializedName("user_id")
    var user_id: Int,
    @SerializedName("userName")
    var userName: String,
)