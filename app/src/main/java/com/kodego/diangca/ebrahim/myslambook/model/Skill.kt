package com.kodego.diangca.ebrahim.myslambook.model

import android.os.Parcel
import android.os.Parcelable

class Skill(var skill: String = "", var rate: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(skill)
        parcel.writeInt(rate)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Skill> {
        override fun createFromParcel(parcel: Parcel): Skill = Skill(parcel)
        override fun newArray(size: Int): Array<Skill?> = arrayOfNulls(size)
    }
}
