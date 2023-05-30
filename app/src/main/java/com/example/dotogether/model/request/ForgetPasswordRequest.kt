package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class ForgetPasswordRequest(
    @SerializedName("email")
    val email: String
)