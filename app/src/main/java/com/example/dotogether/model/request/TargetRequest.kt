package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

open class TargetRequest(
    @SerializedName("target")
    var target: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("period")
    var period: String,
    @SerializedName("start_date")
    var start_date: String,
    @SerializedName("end_date")
    var end_date: String,
    @SerializedName("img")
    var img: String?,
    @SerializedName("tags")
    var tags: String,
    @SerializedName("private")
    val private: Boolean
)