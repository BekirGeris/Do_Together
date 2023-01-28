package com.example.dotogether.model.request

data class SendMessageRequest(
    val chat_id: String,
    val message: String
)