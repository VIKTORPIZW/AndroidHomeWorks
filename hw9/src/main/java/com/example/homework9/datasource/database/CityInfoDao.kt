package com.example.homework9.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityInfoDao {

    @Query("SELECT * FROM cities")
    fun getAllCities(): List<CityEntity>

    @Insert
    fun insertCityEntity(cityEntity: CityEntity)
}