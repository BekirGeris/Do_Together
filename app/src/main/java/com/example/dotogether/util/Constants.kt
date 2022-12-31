package com.example.dotogether.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
object Constants {

    val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd")

    enum class ViewType(val type: Int) {
        VIEW_SHARE_FRAGMENT(1),
        VIEW_PROFILE_FRAGMENT(2),
        VIEW_LIST_CHAT_FRAGMENT(3),
        VIEW_CHAT_FRAGMENT(4),
        VIEW_TARGET_FRAGMENT(5),
        VIEW_REELS_FRAGMENT(6),
        VIEW_FOLLOWS_FRAGMENT(7)
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