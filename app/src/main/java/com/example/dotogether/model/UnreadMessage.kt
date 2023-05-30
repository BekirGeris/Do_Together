package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

data class UnreadMessage(
    @SerializedName("chat_id")
    val chat_id: Int,
    @SerializedName("count")
    val count: Int,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_mute")
    val is_mute: Int,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_id")
    val user_id: Int
)