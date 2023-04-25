package com.example.dotogether.model

class Notification(
    val created_at: String? = null,
    val description: String? = null,
    val id: Int? = null,
    var is_read: Boolean? = null,
    val title: String? = null,
    val type: String? = null,
    val type_id: Int? = null,
    val updated_at: String? = null,
    val img: String? = null,
    val others_img: String? = null,
    val user_id: Int? = null,
    val others_id: Int? = null,
    val others_username: String? = null
)