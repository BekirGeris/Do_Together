package com.example.dotogether.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Basket")
class Basket {
    @PrimaryKey(autoGenerate = true)
    var localId: Int? = null
    var refreshType: String? = null
    var viewType: Int? = null
    var viewId: String? = null

    override fun toString(): String {
        return "Basket(localId=$localId, refreshType=$refreshType)"
    }
}