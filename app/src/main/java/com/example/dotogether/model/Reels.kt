package com.example.dotogether.model

class Reels(
    var created_at: String,
    var id: Int,
    var img: String,
    var updated_at: String,
    var user_id: Int
) {
    constructor() : this("", 0, "", "", 0)
}