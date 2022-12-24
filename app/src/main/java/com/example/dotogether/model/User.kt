package com.example.dotogether.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User() {
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null
    var background_img: String? = null
    var created_at: String? = null
    var description: String? = null
    var email: String? = null
    var email_verified_at: Boolean? = null
    var follower_number: Int? = null
    var following_number: Int? = null
    var id: Int? = null
    var img: String? = null
    var name: String? = null
    var updated_at: String? = null
    var username: String? = null
    var password: String? = null
    var token: String? = null

    constructor(
        name: String?,
        username: String?,
        email: String?,
        password: String?,
        token: String?
    ) : this() {
        this.email = email
        this.name = name
        this.username = username
        this.password = password
        this.token = token
    }
}