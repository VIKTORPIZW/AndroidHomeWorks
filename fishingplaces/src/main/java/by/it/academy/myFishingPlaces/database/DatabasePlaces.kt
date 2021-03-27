package by.it.academy.myFishingPlaces.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.it.academy.myFishingPlaces.entity.PlaceItem
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem

@Database(entities = [PlaceItem::class, PlaceDescriptionItem::class], version = 5)
abstract class DatabasePlaces : RoomDatabase() {

    abstract fun getPlaceDatabaseDAO(): DatabasePlacesDAO
    abstract fun getPlaceDescriptionListDatabaseDAO(): PlaceDescriptionItemDAO

    companion object {
        fun init(context: Context) =
                Room.databaseBuilder(context, DatabasePlaces::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
    }


}