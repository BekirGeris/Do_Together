package com.example.dotogether.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(message: String = "Success", data: T? = null) : Resource<T>(data, message)
    class Error<T>(message: String = "Error", data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}
