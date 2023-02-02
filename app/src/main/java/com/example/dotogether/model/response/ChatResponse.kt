package com.example.dotogether.model.response

import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.UnreadMessage

data class ChatResponse(
    var chat_type: String? = null,
    var chattable_id: Int? = null,
    var created_at: String? = null,
    var id: Int? = null,
    var last_message: String? = null,
    var last_sender_id: Int? = null,
    var otherUser: OtherUser? = null,
    var updated_at: String? = null,
    var user_id: Int? = null,
    var unread_count: Int = 0,
    var is_mute: Int = 0,
    var unread_message: UnreadMessage? = null
)