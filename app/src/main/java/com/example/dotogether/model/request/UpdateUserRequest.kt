package com.example.dotogether.model.request

class UpdateUserRequest(
    var name: String,
    var username: String,
    var email: String,
    var password: String,
    var password_confirmation: String,
    var img: String,
    var background_img: String
)