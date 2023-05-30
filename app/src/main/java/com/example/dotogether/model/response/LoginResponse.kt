package com.example.dotogether.model.response

import com.example.dotogether.model.User
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("token")
    var token: String? = null
    @SerializedName("user")
    var user: User? = null
}