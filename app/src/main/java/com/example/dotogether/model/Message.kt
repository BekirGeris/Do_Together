package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Message(
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("userName")
    var userName: String? = null,
    @SerializedName("userId")
    var userId: Long? = null,
    @SerializedName("messageTime")
    var messageTime: Long? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("isMe")
    var isMe: Boolean = false
) {
    @SerializedName("isUnreadCountMessage")
    var isUnreadCountMessage = false
    @SerializedName("isDateMessage")
    var isDateMessage = false
    @SerializedName("replyMessage")
    var replyMessage: Message? = null
    override fun toString(): String {
        return "Message(key=$key, userName=$userName, userId=$userId, messageTime=$messageTime, message=$message, isMe=$isMe, isUnreadCountMessage=$isUnreadCountMessage, isDateMessage=$isDateMessage, replyMessage=$replyMessage)"
    }
}