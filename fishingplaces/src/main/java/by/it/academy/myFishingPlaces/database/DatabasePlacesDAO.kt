package by.it.academy.myFishingPlaces.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.it.academy.myFishingPlaces.entity.PlaceItem

@Dao
interface DatabasePlacesDAO {

    @Query("SELECT * FROM DataBasePlaces")
    fun getAllPlaces(): MutableList<PlaceItem>

    @Query("SELECT * FROM DataBasePlaces ORDER BY locality")
    fun getAllPlacesSorted(): MutableList<PlaceItem>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlace(place: PlaceItem)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addPlaceToDatabase(place: PlaceItem)
}