package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Notification(
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("is_read")
    var is_read: Boolean? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("type_id")
    val type_id: Int? = null,
    @SerializedName("updated_at")
    val updated_at: String? = null,
    @SerializedName("img")
    val img: String? = null,
    @SerializedName("others_img")
    val others_img: String? = null,
    @SerializedName("user_id")
    val user_id: Int? = null,
    @SerializedName("others_id")
    val others_id: Int? = null,
    @SerializedName("others_username")
    val others_username: String? = null
)