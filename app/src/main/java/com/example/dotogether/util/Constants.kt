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
    val DATE_FORMAT_6 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    const val TOKEN_KEY = "TOKEN_KEY"
    const val FIREBASE_TOKEN = "FIREBASE_TOKEN"

    const val CREATE_TARGET = "CREATE_TARGET"
    const val CHAT = "CHAT"
    const val NOTIFICATION = "NOTIFICATION"
    const val TARGET = "TARGET"
    const val NONE = "NONE"
    const val ACTIVITY = "activity"
    const val USER = "user"

    const val TAG_NOTIFICATION_WORKER = "TAG_NOTIFICATION_WORKER"
    const val DELETE_MESSAGE_FIREBASE_KEY = "DELETE_MESSAGE_FIREBASE_KEY_1221"

    const val PREFIX = "https://targetpalss.page.link"
    const val FILE_PATH_FIRST = "file://"

    //PERIOD CONSTANTS
    const val DAILY = "Daily"
    const val WEEKLY = "Weekly"
    const val MONTHLY = "Monthly"
    const val MONDAY_TO_FRIDAY = "Monday to Friday"
    const val MON = "Mon"
    const val TUE = "Tue"
    const val WED = "Wed"
    const val THU = "Thu"
    const val FRI = "Fri"
    const val SAT = "Sat"
    const val SUN = "Sun"

    //NAVIGATION CONSTANTS
    const val VIEW_TYPE = "viewType"
    const val USERID = "userId"
    const val TARGET_ID = "targetId"
    const val CHAT_ID = "chatId"
    const val CHAT_USER = "chatUser"
    const val IS_GROUP = "isGroup"
    const val IS_EDIT = "isEdit"
    const val IS_ADMIN = "isAdmin"
    const val FOLLOWS_TYPE = "followsType"
    const val E_MAIL = "email"

    //CHATS CONSTANTS
    const val CHATS = "chats"
    const val TIME = "time"
    const val USER_ID = "user_id"
    const val USER_MESSAGE = "user_message"
    const val USERNAME = "username"
    const val MESSAGE_KEY = "message_key"
    const val REPLY_MESSAGE = "reply_message"


    //NOTIFICATION CONSTANTS
    const val NOTIFICATION_DATA = "notification_data"
    const val NOTIFICATION_TYPE = "notification_type"
    const val TYPE_ID = "type_id"

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

    enum class Status(val type: Int) {
        FAILED(-1),
        SUCCESS(0),
        NO_AUTO_LOGIN(1)
    }
}