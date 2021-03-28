package com.example.homework6_1.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.homework6_1.data.Car

@Dao
interface CarDAO {
    @Query("SELECT * FROM car")
    fun getCarsList(): List<Car>

    @Query("SELECT * FROM car WHERE id = :carId")
    fun getCar(carId: Int): Car

    @Insert
    fun insertAll(car: Car)

    @Update
    fun update(car: Car)

    @Delete
    fun delete(car: Car)
}