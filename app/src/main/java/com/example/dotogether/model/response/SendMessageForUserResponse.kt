package com.example.dotogether.model.response

import com.example.dotogether.model.User

data class SendMessageForUserResponse(
    val chat_type: String,
    val chattable_id: Int,
    val chattable_type: String,
    val created_at: String,
    val id: Int,
    val last_message: String,
    val last_sender_id: Int,
    val otherUser: User,
    val updated_at: String,
    val user_id: Int
)
