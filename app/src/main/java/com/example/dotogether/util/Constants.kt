package com.example.dotogether.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
object Constants {

    val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd")

    enum class ViewType {
        VIEW_SHARE_FRAGMENT,
        VIEW_PROFILE_FRAGMENT,
        VIEW_LIST_CHAT_FRAGMENT,
        VIEW_CHAT_FRAGMENT,
        VIEW_TARGET_FRAGMENT,
        VIEW_REELS_FRAGMENT,
        VIEW_FOLLOWS_FRAGMENT
    }

    enum class MethodType {
        METHOD_LOGOUT,
        METHOD_BACKGROUND_EDIT,
        METHOD_PROFILE_EDIT,
        METHOD_JOIN_TARGET,
        METHOD_LIKE_TARGET,
        METHOD_REELS
    }

    enum class Status(val type: Int) {
        FAILED(-1),
        SUCCESS(0),
        NO_AUTO_LOGIN(1)
    }
}