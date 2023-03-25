package com.example.dotogether.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
object Constants {

    val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd")
    val DATE_FORMAT_2 = SimpleDateFormat("yyyy/MM/dd HH:mm")
    val DATE_FORMAT_3 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    val DATE_FORMAT_4 = SimpleDateFormat("HH:mm")
    @SuppressLint("ConstantLocale")
    val DATE_FORMAT_5 = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

    const val TOKEN_KEY = "TOKEN_KEY"
    const val FIREBASE_TOKEN = "FIREBASE_TOKEN"

    const val CREATE_TARGET = "CREATE_TARGET"
    const val CHAT = "CHAT"
    const val NOTIFICATION = "NOTIFICATION"
    const val TARGET = "TARGET"
    const val NONE = "NONE"

    const val TAG_NOTIFICATION_WORKER = "TAG_NOTIFICATION_WORKER"
    const val DELETE_MESSAGE_FIREBASE_KEY = "DELETE_MESSAGE_FIREBASE_KEY_1221"

    const val PREFIX = "https://targetpalss.page.link"

    enum class ViewType(val type: Int) {
        VIEW_SHARE_FRAGMENT(1),
        VIEW_PROFILE_FRAGMENT(2),
        VIEW_LIST_CHAT_FRAGMENT(3),
        VIEW_CHAT_FRAGMENT(4),
        VIEW_TARGET_FRAGMENT(5),
        VIEW_FOLLOWS_FRAGMENT(6),
        VIEW_SEARCH_FRAGMENT(7),
        VIEW_USER_EDIT_FRAGMENT(8),
        VIEW_PASSWORD_EDIT_FRAGMENT(9),
        VIEW_NOTIFICATION_FRAGMENT(10),
        VIEW_ADD_TAG_FRAGMENT(11)
    }

    enum class MethodType {
        METHOD_LOGOUT,
        METHOD_BACKGROUND_EDIT,
        METHOD_PROFILE_EDIT,
        METHOD_JOIN_TARGET,
        METHOD_UN_JOIN_TARGET,
        METHOD_LIKE_TARGET,
        METHOD_UN_LIKE_TARGET,
        METHOD_REELS
    }

    enum class Status(val type: Int) {
        FAILED(-1),
        SUCCESS(0),
        NO_AUTO_LOGIN(1)
    }
}