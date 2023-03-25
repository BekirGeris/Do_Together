package com.example.dotogether.model

class Message(
    var key: String? = null,
    var userName: String? = null,
    var userId: Long? = null,
    var messageTime: String? = null,
    var message: String? = null,
    var isMe: Boolean = false
) {
    var isUnreadCountMessage = false
    var isDateMessage = false
}