package com.example.dotogether.model.request

data class ForgetPasswordVerifyRequest(
    val email: String,
    val otp: String,
    val password: String,
    val password_confirmation: String
)