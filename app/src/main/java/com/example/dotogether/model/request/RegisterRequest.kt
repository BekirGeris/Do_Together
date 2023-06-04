package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName
import java.util.*

class RegisterRequest(
    @SerializedName("name")
    var name: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("password_confirmation")
    var password_confirmation: String
)