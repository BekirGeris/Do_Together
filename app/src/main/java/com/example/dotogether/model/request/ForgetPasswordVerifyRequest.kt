package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class ForgetPasswordVerifyRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("password_confirmation")
    val password_confirmation: String
)