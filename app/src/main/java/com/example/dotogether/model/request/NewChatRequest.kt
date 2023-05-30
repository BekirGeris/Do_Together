package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class NewChatRequest(
    @SerializedName("receiver_username")
    val receiver_username: String,
    @SerializedName("message")
    val message: String
)