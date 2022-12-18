package com.example.dotogether.model.response

class Response<T>(
    val success: Boolean,
    val message: String,
    val data: T,
    val errors: Errors
)