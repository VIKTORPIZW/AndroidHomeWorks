package com.example.homework9.datasource.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities" )
class CityEntity (
        @PrimaryKey(autoGenerate = true) val id: Long,
        @ColumnInfo(name ="city_name") val cityName: String?
        )