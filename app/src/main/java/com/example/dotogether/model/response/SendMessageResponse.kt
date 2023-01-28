package com.example.dotogether.model.response

import com.example.dotogether.model.Target

data class SendMessageResponse(
    val chat_type: String,
    val chattable_id: Int,
    val chattable_type: String,
    val created_at: String,
    val id: Int,
    val last_message: String,
    val last_sender_id: Int,
    val otherUser: Target,
    val updated_at: String,
    val user_id: Int
)