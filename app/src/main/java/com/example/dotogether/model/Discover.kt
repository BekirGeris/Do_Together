package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Discover {
    @SerializedName("type")
    var type: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("activities")
    var targets: ArrayList<Target>? = null
}