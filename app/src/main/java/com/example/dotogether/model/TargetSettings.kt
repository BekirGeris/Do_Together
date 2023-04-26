package com.example.dotogether.model

import android.os.Parcel
import android.os.Parcelable

class TargetSettings() : Parcelable {
    var notify: Int? = null
    var autosend: Int? = null

    constructor(parcel: Parcel) : this() {
        notify = parcel.readValue(Int::class.java.classLoader) as? Int
        autosend = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(notify)
        parcel.writeValue(autosend)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TargetSettings> {
        override fun createFromParcel(parcel: Parcel): TargetSettings {
            return TargetSettings(parcel)
        }

        override fun newArray(size: Int): Array<TargetSettings?> {
            return arrayOfNulls(size)
        }
    }
}