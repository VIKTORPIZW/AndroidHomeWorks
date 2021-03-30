package by.it.academy.app8_2task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.it.academy.app8_2task.entity.PlaceItem
import by.it.academy.app8_2task.entity.PlaceDescriptionItem

@Database(entities = [PlaceItem::class, PlaceDescriptionItem::class], version = 2)
abstract class DatabasePlaces : RoomDatabase() {
    abstract fun getPlaceDatabaseDAO(): DatabasePlacesDAO
    abstract fun getPlaceDescriptionListDatabaseDAO(): PlaceDescriptionItemDAO
    companion object {
        fun init(context: Context) =
                Room.databaseBuilder(context, DatabasePlaces::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}