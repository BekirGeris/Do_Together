package com.example.dotogether.model.response

import com.example.dotogether.model.OtherUser

data class NewChatResponse(
    val chat_type: String,
    val chattable_id: Int,
    val chattable_type: String,
    val created_at: String,
    val id: String,
    val last_message: String,
    val last_sender_id: Int,
    val otherUser: OtherUser,
    val updated_at: String,
    val user_id: Int
)
