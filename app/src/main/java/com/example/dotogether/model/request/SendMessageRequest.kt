package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class SendMessageRequest(
    @SerializedName("chat_id")
    val chat_id: String,
    @SerializedName("message")
    val message: String
)