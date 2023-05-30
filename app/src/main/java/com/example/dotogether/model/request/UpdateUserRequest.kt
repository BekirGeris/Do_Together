package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

class UpdateUserRequest(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("password_confirmation")
    var password_confirmation: String? = null,
    @SerializedName("img")
    var img: String? = null,
    @SerializedName("background_img")
    var background_img: String? = null,
    @SerializedName("fcm_token")
    var fcm_token: String? = null,
    @SerializedName("tags")
    var tags: String? = null
)