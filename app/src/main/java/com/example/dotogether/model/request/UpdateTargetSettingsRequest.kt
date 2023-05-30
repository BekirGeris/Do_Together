package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class UpdateTargetSettingsRequest(
    @SerializedName("autosend")
    val autosend: Int,
    @SerializedName("notify")
    val notify: Int
)