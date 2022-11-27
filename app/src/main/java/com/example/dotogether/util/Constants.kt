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
}