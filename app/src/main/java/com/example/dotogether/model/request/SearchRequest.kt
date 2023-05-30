package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class SearchRequest(
    @SerializedName("search")
    val search: String
)