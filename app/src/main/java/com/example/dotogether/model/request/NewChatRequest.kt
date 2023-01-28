package com.example.dotogether.model.request

data class NewChatRequest(
    val receiver_username: String,
    val message: String
)