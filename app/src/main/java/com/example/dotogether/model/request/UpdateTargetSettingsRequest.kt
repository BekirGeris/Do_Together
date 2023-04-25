package com.example.dotogether.model.request

data class UpdateTargetSettingsRequest(
    val autosend: Int,
    val notify: Int
)