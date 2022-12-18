package com.example.dotogether.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    var name: String?,
    var username: String?,
    var email: String?,
    var password: String?,
    var token: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor() : this("", "", "", "", "")
}