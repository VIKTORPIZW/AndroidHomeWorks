package by.it.academy.app8_2task.repositories

import android.content.Context
import by.it.academy.app8_2task.database.DatabasePlaces
import by.it.academy.app8_2task.entity.PlaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabasePlaceRepository(context: Context) {

    private val dao = DatabasePlaces.init(context)

    suspend fun getAllPlacesSortedByLocality() = withContext(Dispatchers.IO) { dao.getPlaceDatabaseDAO().getAllPlacesSorted() }

    suspend fun addPlace(placeItem: PlaceItem) {
        withContext(Dispatchers.IO) { dao.getPlaceDatabaseDAO().addPlaceToDatabase(placeItem) }
    }

    suspend fun updatePlace(placeItem: PlaceItem) {
        withContext(Dispatchers.IO) { dao.getPlaceDatabaseDAO().updatePlace(placeItem) }
    }
}

