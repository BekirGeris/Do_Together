package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName
import java.util.Locale

class LoginRequest(
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String
)