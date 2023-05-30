package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)