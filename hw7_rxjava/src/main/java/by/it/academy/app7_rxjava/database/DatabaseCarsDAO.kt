package by.it.academy.app7_rxjava.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.it.academy.app7_rxjava.entity.CarItem

@Dao
interface DatabaseCarsDAO {

    @Query("SELECT * FROM DataBaseCars")
    fun getAllCars(): MutableList<CarItem>

    @Query("SELECT * FROM DataBaseCars ORDER BY producer")
    fun getAllCarsSorted(): MutableList<CarItem>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCar(car: CarItem)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addCarToDatabase(car: CarItem)
}