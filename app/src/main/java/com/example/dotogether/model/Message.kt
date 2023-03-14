package com.example.dotogether.model

class Message (
    var userName: String? = null,
    var messageTime: String? = null,
    var message: String? = null,
    var isMe: Boolean = false
) {
    var isUnreadCountMessage = false
    var isDateMessage = false
}