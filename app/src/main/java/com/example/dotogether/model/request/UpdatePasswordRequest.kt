package com.example.dotogether.model.request

data class UpdatePasswordRequest(
    val old_password: String,
    val new_password: String,
    val new_password_confirmation: String
)