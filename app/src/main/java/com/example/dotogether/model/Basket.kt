package com.example.dotogether.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Basket")
class Basket {
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null
    var totalUnreadCount: Int = 0

    override fun toString(): String {
        return "Basket(localId=$localId, totalUnreadCount=$totalUnreadCount)"
    }
}