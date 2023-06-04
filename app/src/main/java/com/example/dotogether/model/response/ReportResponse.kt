package com.example.dotogether.model.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(
    @SerializedName("activity_id")
    val activity_id: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("created_at")
    val created_at: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_resolved")
    val is_resolved: Boolean,
    @SerializedName("report_type_id")
    val report_type_id: String,
    @SerializedName("resolved_by")
    val resolved_by: Any,
    @SerializedName("updated_at")
    val updated_at: String,
    @SerializedName("user_id")
    val user_id: Int
)