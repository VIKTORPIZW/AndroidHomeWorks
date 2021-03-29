package by.it.academy.app8_2task.repositories

import android.content.Context
import by.it.academy.app8_2task.database.DatabasePlaces
import by.it.academy.app8_2task.entity.PlaceDescriptionItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabasePlaceDescriptionRepository(context: Context) {
    private val dao = DatabasePlaces.init(context)
    suspend fun getPlaceDescriptionList(fishSpecies: String) = withContext(Dispatchers.IO) {
        dao.getPlaceDescriptionListDatabaseDAO().getPlaceDescriptionList(fishSpecies)
    }
    suspend fun addPlaceDescription(placeDescriptionItem: PlaceDescriptionItem) {
        withContext(Dispatchers.IO) { dao.getPlaceDescriptionListDatabaseDAO().addPlaceDescription(placeDescriptionItem) }
    }
    suspend fun updatePlaceDescription(placeDescriptionItem: PlaceDescriptionItem) {
        withContext(Dispatchers.IO) { dao.getPlaceDescriptionListDatabaseDAO().updatePlaceDescription(placeDescriptionItem) }
    }
    suspend fun deletePlaceDescription(placeDescriptionItem: PlaceDescriptionItem) {
        withContext(Dispatchers.IO) { dao.getPlaceDescriptionListDatabaseDAO().deletePlaceDescription(placeDescriptionItem) }
    }
    suspend fun getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpecies: String, fishingStatus: Int) = withContext(Dispatchers.IO) {
        dao.getPlaceDescriptionListDatabaseDAO().getFilteredPlaceDescriptionListByPlaceDescriptionType(fishSpecies, fishingStatus)
    }
}