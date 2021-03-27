package by.it.academy.myFishingPlaces.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DataBasePlaces")
class PlaceItem(
        @ColumnInfo val placeName: String,
        @ColumnInfo val locality: String,
        @ColumnInfo val fishingType: String,
        @PrimaryKey @ColumnInfo val fishSpecies: String,
        @ColumnInfo val pathImage: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(placeName)
        parcel.writeString(locality)
        parcel.writeString(fishingType)
        parcel.writeString(fishSpecies)
        parcel.writeString(pathImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceItem> {
        override fun createFromParcel(parcel: Parcel): PlaceItem {
            return PlaceItem(parcel)
        }

        override fun newArray(size: Int): Array<PlaceItem?> {
            return arrayOfNulls(size)
        }
    }

}