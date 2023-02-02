package com.example.dotogether.model

data class UnreadMessage(
    val chat_id: Int,
    val count: Int,
    val created_at: String,
    val id: Int,
    val is_mute: Int,
    val updated_at: String,
    val user_id: Int
)