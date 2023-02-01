package com.example.dotogether.model

import kotlin.random.Random
import kotlin.random.nextInt

class Target {
    var admin: User? = null
    var created_at: String? = null
    var description: String? = null
    var end_date: String? = null
    var id: Int? = null
    var img: String? = null
    var is_done: Int? = null
    var is_joined: Boolean? = null
    var is_liked: Boolean? = null
    var period: String? = null
    var room_id: Int? = null
    var start_date: String? = null
    var target: String? = null
    var updated_at: String? = null
    var user_id: Int? = null
    var users: ArrayList<User>? = null
    var chat: String? = null
    var unread_count: Int? = null
}