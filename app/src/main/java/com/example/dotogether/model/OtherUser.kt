package com.example.dotogether.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class OtherUser() : Parcelable {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("target")
    var target: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("email_verified_at")
    var email_verified_at: Boolean? = null
    @SerializedName("created_at")
    var created_at: String? = null
    @SerializedName("updated_at")
    var updated_at: String? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("img")
    var img: String? = null
    @SerializedName("background_img")
    var background_img: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("fcm_token")
    var fcm_token: String? = null
    @SerializedName("tags")
    var tags: String? = null
    @SerializedName("follower_number")
    var follower_number: Int? = null
    @SerializedName("following_number")
    var following_number: Int? = null
    @SerializedName("is_followed")
    var is_followed: Boolean? = null
    @SerializedName(value = "chat_id", alternate = ["chat"])
    var chat_id: String? = null

    constructor(user: User) : this() {
        this.background_img = user.background_img
        this.created_at = user.created_at
        this.description = user.description
        this.email = user.email
        this.email_verified_at = user.email_verified_at
        this.follower_number = user.follower_number
        this.following_number = user.following_number
        this.id = user.id
        this.img = user.img
        this.name = user.name
        this.username = user.username
        this.updated_at = user.updated_at
        this.chat_id = user.chat_id
    }

    constructor(target: Target) : this() {
        this.created_at = target.created_at
        this.description = target.description
        this.chat_id = target.chat
        this.target = target.target
        this.id = target.id
        this.img = target.img
        this.updated_at = target.updated_at
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        target = parcel.readString()
        email = parcel.readString()
        email_verified_at = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        created_at = parcel.readString()
        updated_at = parcel.readString()
        username = parcel.readString()
        img = parcel.readString()
        background_img = parcel.readString()
        description = parcel.readString()
        fcm_token = parcel.readString()
        tags = parcel.readString()
        follower_number = parcel.readValue(Int::class.java.classLoader) as? Int
        following_number = parcel.readValue(Int::class.java.classLoader) as? Int
        is_followed = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        chat_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(target)
        parcel.writeString(email)
        parcel.writeValue(email_verified_at)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(username)
        parcel.writeString(img)
        parcel.writeString(background_img)
        parcel.writeString(description)
        parcel.writeString(fcm_token)
        parcel.writeString(tags)
        parcel.writeValue(follower_number)
        parcel.writeValue(following_number)
        parcel.writeValue(is_followed)
        parcel.writeString(chat_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OtherUser> {
        override fun createFromParcel(parcel: Parcel): OtherUser {
            return OtherUser(parcel)
        }

        override fun newArray(size: Int): Array<OtherUser?> {
            return arrayOfNulls(size)
        }
    }
}