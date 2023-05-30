package com.example.dotogether.model

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class Errors {
    @SerializedName("username")
    var username: ArrayList<String>? = null
    @SerializedName("email")
    var email: ArrayList<String>? = null
    @SerializedName("description")
    var description: ArrayList<String>? = null
}