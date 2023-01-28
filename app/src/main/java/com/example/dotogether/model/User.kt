package com.example.dotogether.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User() : Parcelable {
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
    @Ignore
    var active_statuses: ArrayList<Reels>? = null
    @Ignore
    var activities: ArrayList<Target>? = null
    @Ignore
    var is_followed: Boolean? = null
    var token: String? = null
    @Ignore
    var pivot: Pivot? = null
    var chat_id: String? = null

    constructor(parcel: Parcel) : this() {
        localId = parcel.readValue(Int::class.java.classLoader) as? Int
        background_img = parcel.readString()
        created_at = parcel.readString()
        description = parcel.readString()
        email = parcel.readString()
        email_verified_at = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        follower_number = parcel.readValue(Int::class.java.classLoader) as? Int
        following_number = parcel.readValue(Int::class.java.classLoader) as? Int
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        img = parcel.readString()
        name = parcel.readString()
        updated_at = parcel.readString()
        username = parcel.readString()
        password = parcel.readString()
        is_followed = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        token = parcel.readString()
        chat_id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(localId)
        parcel.writeString(background_img)
        parcel.writeString(created_at)
        parcel.writeString(description)
        parcel.writeString(email)
        parcel.writeValue(email_verified_at)
        parcel.writeValue(follower_number)
        parcel.writeValue(following_number)
        parcel.writeValue(id)
        parcel.writeString(img)
        parcel.writeString(name)
        parcel.writeString(updated_at)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeValue(is_followed)
        parcel.writeString(token)
        parcel.writeString(chat_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}