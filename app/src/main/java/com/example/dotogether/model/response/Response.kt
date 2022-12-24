package com.example.dotogether.model.response

import com.example.dotogether.model.Errors

class Response<T> {
    var success: Boolean = false
    var message: String = ""
    var data: T? = null
    var errors: Errors? = null
}