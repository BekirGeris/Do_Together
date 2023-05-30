package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("active")
    var active: Boolean? = null,
    @SerializedName("label")
    var label: String? = null,
    @SerializedName("url")
    var url: String? = null
)