    package com.kodego.diangca.ebrahim.myslambook.model

    import android.os.Parcel
    import android.os.Parcelable
    import com.kodego.diangca.ebrahim.myslambook.model.Hobbies

    class Movie(var movieName: String = "") : Parcelable {


        constructor(parcel: Parcel) : this(
            parcel.readString() ?: ""
        )


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(movieName)
        }


        override fun describeContents(): Int = 0


        companion object CREATOR : Parcelable.Creator<Movie> {
            override fun createFromParcel(parcel: Parcel): Movie = Movie(parcel)
            override fun newArray(size: Int): Array<Movie?> = arrayOfNulls(size)
        }

    }