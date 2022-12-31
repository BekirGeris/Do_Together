package com.example.dotogether.model

data class Connection(
    val active_statuses: ArrayList<Reels>,
    val background_img: String,
    val created_at: String,
    val description: String,
    val email: String,
    val email_verified_at: Any,
    val follower_number: Int,
    val following_number: Int,
    val id: Int,
    val img: String,
    val name: String,
    val pivot: Pivot,
    val updated_at: String,
    val username: String
)