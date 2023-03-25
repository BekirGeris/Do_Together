package com.example.dotogether.model

import android.os.Parcel
import android.os.Parcelable

class NotificationData(
    var type: String? = null,
    var typeId: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        typeId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(typeId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationData> {
        override fun createFromParcel(parcel: Parcel): NotificationData {
            return NotificationData(parcel)
        }

        override fun newArray(size: Int): Array<NotificationData?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "NotificationData(type=$type, typeId=$typeId)"
    }}
