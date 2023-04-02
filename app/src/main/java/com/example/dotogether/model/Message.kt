package com.example.dotogether.model

class Message(
    var key: String? = null,
    var userName: String? = null,
    var userId: Long? = null,
    var messageTime: Long? = null,
    var message: String? = null,
    var isMe: Boolean = false
) {
    var isUnreadCountMessage = false
    var isDateMessage = false
    var replyMessage: Message? = null
    override fun toString(): String {
        return "Message(key=$key, userName=$userName, userId=$userId, messageTime=$messageTime, message=$message, isMe=$isMe, isUnreadCountMessage=$isUnreadCountMessage, isDateMessage=$isDateMessage, replyMessage=$replyMessage)"
    }
}