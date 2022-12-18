package com.example.dotogether.model.request

class RegisterRequest(
    var name: String,
    var username: String,
    var email: String,
    var password: String,
    var password_confirmation: String
)