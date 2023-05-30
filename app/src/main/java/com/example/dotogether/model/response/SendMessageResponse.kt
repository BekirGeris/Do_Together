package com.example.dotogether.model.response

import com.example.dotogether.model.Target
import com.google.gson.annotations.SerializedName

data class SendMessageResponse(
    @SerializedName("chat_type")
    val chat_type: String,
    @SerializedName("chattable_id")
    val chattable_id: Int,
    @SerializedName("chattable_type")
    val chattable_type: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_message")
    val last_message: String,
    @SerializedName("last_sender_id")
    val last_sender_id: Int,
    @SerializedName("otherUser")
    val otherUser: Target,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_id")
    val user_id: Int
)