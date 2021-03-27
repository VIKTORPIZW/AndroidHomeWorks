package by.it.academy.app7_completablefuture.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DataBaseCars")
class CarItem(
        @ColumnInfo val customerName: String,
        @ColumnInfo val producer: String,
        @ColumnInfo val carModel: String,
        @PrimaryKey @ColumnInfo val carPlate: String,
        @ColumnInfo val pathImage: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(customerName)
        parcel.writeString(producer)
        parcel.writeString(carModel)
        parcel.writeString(carPlate)
        parcel.writeString(pathImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarItem> {
        override fun createFromParcel(parcel: Parcel): CarItem {
            return CarItem(parcel)
        }

        override fun newArray(size: Int): Array<CarItem?> {
            return arrayOfNulls(size)
        }
    }

}