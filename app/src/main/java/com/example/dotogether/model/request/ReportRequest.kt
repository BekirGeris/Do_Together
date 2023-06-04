package com.example.dotogether.model.request

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("reportId")
    var reportId: String,
    @SerializedName("comment")
    var comment: String
)
