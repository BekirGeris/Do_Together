package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Target {
    @SerializedName("admin")
    var admin: User? = null
    @SerializedName("created_at")
    var created_at: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("end_date")
    var end_date: String? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("img")
    var img: String? = null
    @SerializedName("is_done")
    var is_done: Int? = null
    @SerializedName("is_joined")
    var is_joined: Boolean? = null
    @SerializedName("is_liked")
    var is_liked: Boolean? = null
    @SerializedName("period")
    var period: String? = null
    @SerializedName("room_id")
    var room_id: Int? = null
    @SerializedName("start_date")
    var start_date: String? = null
    @SerializedName("target")
    var target: String? = null
    @SerializedName("updated_at")
    var updated_at: String? = null
    @SerializedName("user_id")
    var user_id: Int? = null
    @SerializedName(value = "users", alternate = ["users_limited"])
    var users: ArrayList<User>? = null
    @SerializedName(value = "chat", alternate = ["chat_id"])
    var chat_id: String? = null
    @SerializedName("unread_count")
    var unread_count: Int = 0
    @SerializedName("action_status")
    var action_status: String? = null
    @SerializedName("tags")
    var tags: String? = null
    @SerializedName("last_date")
    var last_date: String? = null
    @SerializedName("private")
    var private: Boolean? = null
    @SerializedName("settings")
    var settings: TargetSettings? = null
}