package com.example.dotogether.model.request

data class SendMessageForUserRequest(
    val receiver_username: String,
    val message: String
)