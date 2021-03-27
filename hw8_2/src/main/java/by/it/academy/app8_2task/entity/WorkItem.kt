package by.it.academy.app8_2task.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WorksDataBase")
class WorkItem(
        @ColumnInfo val applicationDate: String,
        @ColumnInfo val workType: String,
        @ColumnInfo val description: String,
        @ColumnInfo val cost: Float,
        @ColumnInfo val workStatus: Int,
        @ColumnInfo val carPlate: String,
        @PrimaryKey(autoGenerate = true) @ColumnInfo var id: Long = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readString().toString(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(applicationDate)
        parcel.writeString(workType)
        parcel.writeString(description)
        parcel.writeFloat(cost)
        parcel.writeInt(workStatus)
        parcel.writeString(carPlate)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WorkItem> {
        override fun createFromParcel(parcel: Parcel): WorkItem {
            return WorkItem(parcel)
        }

        override fun newArray(size: Int): Array<WorkItem?> {
            return arrayOfNulls(size)
        }
    }
}