package by.it.academy.app7_completablefuture.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import by.it.academy.app7_completablefuture.entity.CarItem
import by.it.academy.app7_completablefuture.entity.WorkItem

@Database(entities = [CarItem::class, WorkItem::class], version = 1)
abstract class DatabaseCars : RoomDatabase() {

    abstract fun getCarDatabaseDAO(): DatabaseCarsDAO
    abstract fun getWorkListDatabaseDAO(): WorkItemDAO

    companion object {
        fun init(context: Context) =
                Room.databaseBuilder(context, DatabaseCars::class.java, "database")
                        .fallbackToDestructiveMigration()
                        .build()
    }


}