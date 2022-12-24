package com.example.dotogether.util

sealed class Resource<T>(val code: Constants.Status? = null, val data: T? = null, val message: String? = null) {
    class Success<T>(code: Constants.Status? = Constants.Status.SUCCESS, message: String? = "Success", data: T? = null) : Resource<T>(code, data, message)
    class Error<T>(code: Constants.Status? = Constants.Status.FAILED, message: String? = "Error", data: T? = null) : Resource<T>(code, data, message)
    class Loading<T> : Resource<T>()
}
