package com.example.dotogether.model.request

open class TargetRequest(
    var target: String,
    var description: String,
    var period: String,
    var start_date: String,
    var end_date: String,
    var img: String?,
    var tags: String,
    val private: Boolean
)