package com.example.dotogether.model.request

data class UpdatePasswordRequest(
    val new_password: String,
    val new_password_confirmation: String,
    val old_password: String
)