package com.example.dotogether.model.response

import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.UnreadMessage
import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("chat_type")
    var chat_type: String? = null,
    @SerializedName("chattable_id")
    var chattable_id: Int? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("last_message")
    var last_message: String? = null,
    @SerializedName("last_sender_id")
    var last_sender_id: Int? = null,
    @SerializedName("otherUser")
    var otherUser: OtherUser? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("user_id")
    var user_id: Int? = null,
    @SerializedName("unread_count")
    var unread_count: Int = 0,
    @SerializedName("is_mute")
    var is_mute: Int = 0,
    @SerializedName("unread_message")
    var unread_message: UnreadMessage? = null
)