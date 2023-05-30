package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Page<T>(
    @SerializedName("current_page")
    var current_page: Int? = null,
    @SerializedName("data")
    var data: ArrayList<T>? = null,
    @SerializedName("first_page_url")
    var first_page_url: String? = null,
    @SerializedName("from")
    var from: Int? = null,
    @SerializedName("last_page")
    var last_page: Int? = null,
    @SerializedName("last_page_url")
    var last_page_url: String? = null,
    @SerializedName("links")
    var links: List<Link>? = null,
    @SerializedName("next_page_url")
    var next_page_url: String? = null,
    @SerializedName("path")
    var path: String? = null,
    @SerializedName("per_page")
    var per_page: Int? = null,
    @SerializedName("prev_page_url")
    var prev_page_url: String? = null,
    @SerializedName("to")
    var to: Int? = null,
    @SerializedName("total")
    var total: Int? = null
)