package com.example.dotogether.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Basket")
class Basket {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("localId")
    var localId: Int? = null
    @SerializedName("refreshType")
    var refreshType: String? = null
    @SerializedName("viewType")
    var viewType: Int? = null
    @SerializedName("viewId")
    var viewId: String? = null

    override fun toString(): String {
        return "Basket(localId=$localId, refreshType=$refreshType)"
    }
}