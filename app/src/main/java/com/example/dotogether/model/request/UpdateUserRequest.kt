package com.example.dotogether.model.request

class UpdateUserRequest(
    var name: String? = null,
    var username: String? = null,
    var description: String? = null,
    var email: String? = null,
    var password: String? = null,
    var password_confirmation: String? = null,
    var img: String? = null,
    var background_img: String? = null,
    var fcm_token: String? = null,
    var tags: String? = null
)