package by.it.academy.myFishingPlaces.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlaceDescriptionDataBase")
class PlaceDescriptionItem(
        @ColumnInfo val applicationDate: String,
        @ColumnInfo val roadToWater: String,
        @ColumnInfo val description: String,
        @ColumnInfo val distance: Float,
        @ColumnInfo val fishingStatus: Int,
        @ColumnInfo val fishSpecies: String,
        @PrimaryKey(autoGenerate = true) @ColumnInfo var id: Long = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(applicationDate)
        parcel.writeString(roadToWater)
        parcel.writeString(description)
        parcel.writeFloat(distance)
        parcel.writeInt(fishingStatus)
        parcel.writeString(fishSpecies)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceDescriptionItem> {
        override fun createFromParcel(parcel: Parcel): PlaceDescriptionItem {
            return PlaceDescriptionItem(parcel)
        }

        override fun newArray(size: Int): Array<PlaceDescriptionItem?> {
            return arrayOfNulls(size)
        }
    }
}