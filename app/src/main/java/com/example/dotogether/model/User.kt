package com.example.dotogether.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
class User() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("localId")
    var localId: Int? = null
    @SerializedName("background_img")
    var background_img: String? = null
    @SerializedName("created_at")
    var created_at: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("email_verified_at")
    var email_verified_at: Boolean? = null
    @SerializedName("follower_number")
    var follower_number: Int? = null
    @SerializedName("following_number")
    var following_number: Int? = null
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("img")
    var img: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("updated_at")
    var updated_at: String? = null
    @SerializedName("username")
    var username: String? = null
    @SerializedName("password")
    var password: String? = null
    @SerializedName("active_statuses")
    @Ignore
    var active_statuses: ArrayList<Reels>? = null
    @SerializedName("activities")
    @Ignore
    var activities: ArrayList<Target>? = null
    @SerializedName("is_followed")
    @Ignore
    var is_followed: Boolean? = null
    @SerializedName("token")
    var token: String? = null
    @SerializedName("fcm_token")
    var fcm_token: String? = null
    @SerializedName("pivot")
    @Ignore
    var pivot: Pivot? = null
    @SerializedName("chat_id")
    var chat_id: String? = null
    @SerializedName("unread_count")
    @Ignore
    var unread_count: Int = 0
    @SerializedName("notification_count")
    @Ignore
    var notification_count: Int = 0
    @SerializedName("tags")
    @Ignore
    var tags: String? = null

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
        unread_count = parcel.readValue(Int::class.java.classLoader) as Int
        notification_count = parcel.readValue(Int::class.java.classLoader) as Int
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
        parcel.writeValue(unread_count)
        parcel.writeValue(notification_count)
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