package by.it.academy.app7_coroutines.repositories

import android.content.Context
import by.it.academy.app7_coroutines.database.DatabaseCars
import by.it.academy.app7_coroutines.entity.CarItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabaseCarsRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    suspend fun getAllCarsSortedByProducer() = withContext(Dispatchers.IO) { dao.getCarDatabaseDAO().getAllCarsSorted() }

    suspend fun addCar(carItem: CarItem) {
        withContext(Dispatchers.IO) { dao.getCarDatabaseDAO().addCarToDatabase(carItem) }
    }

    suspend fun updateCar(carItem: CarItem) {
        withContext(Dispatchers.IO) { dao.getCarDatabaseDAO().updateCar(carItem) }
    }
}

