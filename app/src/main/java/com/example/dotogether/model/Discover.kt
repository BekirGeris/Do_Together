package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

class Discover {
    var type: String? = null
    var title: String? = null
    @SerializedName("activities")
    var targets: ArrayList<Target>? = null
}