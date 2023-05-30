package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("old_password")
    val old_password: String,
    @SerializedName("new_password")
    val new_password: String,
    @SerializedName("new_password_confirmation")
    val new_password_confirmation: String
)