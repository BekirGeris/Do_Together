package com.example.dotogether.model

import com.google.gson.annotations.SerializedName

data class Action(
    @SerializedName("activity_id")
    val activity_id: Int? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("expires_at")
    val expires_at: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("is_expired")
    val is_expired: Int? = null,
    @SerializedName("pushed_at")
    val pushed_at: String? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("updated_at")
    val updated_at: String? = null,
    @SerializedName("user_id")
    val user_id: Int? = null
)