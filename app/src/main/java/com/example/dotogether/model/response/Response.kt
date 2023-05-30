package com.example.dotogether.model.response

import com.example.dotogether.model.Errors
import com.google.gson.annotations.SerializedName

class Response<T> {
    @SerializedName("success")
    var success: Boolean = false
    @SerializedName("message")
    var message: String = ""
    @SerializedName("data")
    var data: T? = null
    @SerializedName("errors")
    var errors: Errors? = null
}