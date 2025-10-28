package com.kodego.diangca.ebrahim.myslambook.model

import android.os.Parcel
import android.os.Parcelable

class Hobbies(var hobbie: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hobbie)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Hobbies> {
        override fun createFromParcel(parcel: Parcel): Hobbies = Hobbies(parcel)
        override fun newArray(size: Int): Array<Hobbies?> = arrayOfNulls(size)
    }
}
