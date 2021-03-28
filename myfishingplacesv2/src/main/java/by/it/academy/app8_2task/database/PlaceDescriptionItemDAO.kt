package by.it.academy.app8_2task.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.it.academy.app8_2task.entity.PlaceDescriptionItem

@Dao
interface PlaceDescriptionItemDAO {

    @Query("SELECT * FROM PlaceDescriptionsDataBase WHERE fishSpecies = :fishSpecies")
    fun getPlaceDescriptionList(fishSpecies: String): MutableList<PlaceDescriptionItem>

    @Insert
    fun addPlaceDescription(entity: PlaceDescriptionItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaceDescription(placeDescriptionItem:PlaceDescriptionItem)

    @Delete
    fun deletePlaceDescription(placeDescriptionItem: PlaceDescriptionItem)

    @Query("SELECT * FROM PlaceDescriptionsDataBase WHERE fishSpecies =:fishSpaces AND fishingStatus =:fishingStatus")
    fun getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpaces: String, fishingStatus:Int) :MutableList<PlaceDescriptionItem>

}