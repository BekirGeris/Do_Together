package com.example.dotogether.model.request

data class SendMessageForTargetRequest(
    val chat_id: Int,
    val message: String
)