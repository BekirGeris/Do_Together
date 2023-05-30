package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

data class Tag(
    @SerializedName("name")
    val name: String
)