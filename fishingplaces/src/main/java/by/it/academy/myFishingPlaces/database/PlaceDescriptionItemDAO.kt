package by.it.academy.myFishingPlaces.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.it.academy.myFishingPlaces.entity.PlaceDescriptionItem

@Dao
interface PlaceDescriptionItemDAO {

    @Query("SELECT * FROM PlaceDescriptionDataBase WHERE fishSpecies = :carPlate")
    fun getPlaceDescriptionList(carPlate:String): MutableList<PlaceDescriptionItem>

    @Insert
    fun addPlaceDescription(entity: PlaceDescriptionItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaceDescription(placeDescriptionItem:PlaceDescriptionItem)

    @Delete
    fun deletePlaceDescription(placeDescriptionItem: PlaceDescriptionItem)

    @Query("SELECT * FROM PlaceDescriptionDataBase WHERE fishSpecies = :fishSpecies AND fishingStatus =:fishingStatus")
    fun getFilteredPlaceDescriptionListByPlaceType(fishSpecies: String, fishingStatus:Int) :MutableList<PlaceDescriptionItem>

}