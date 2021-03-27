package com.example.homework9.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class],version = 1)
abstract class DataBaseInfo: RoomDatabase() {

    abstract fun getCityInfoDao(): CityInfoDao

    companion object{
        private var DB: DataBaseInfo? = null

        fun getInstance(context: Context): DataBaseInfo? {
            if(DB == null){
                DB = Room.databaseBuilder(context, DataBaseInfo::class.java, "db_cities")
                        .build()
            }
            return DB
        }
    }

}