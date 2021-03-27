package by.it.academy.app7_completablefuture.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import by.it.academy.app7_completablefuture.database.DatabaseCars
import by.it.academy.app7_completablefuture.entity.CarItem
import java.util.concurrent.CompletableFuture

class DatabaseCarsRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    fun getAllCarsSortedByProducer(): CompletableFuture<MutableList<CarItem>> = CompletableFuture.supplyAsync {
        return@supplyAsync dao.getCarDatabaseDAO().getAllCarsSorted()
    }

    fun addCar(carItem: CarItem) {
        CompletableFuture.runAsync {
            dao.getCarDatabaseDAO().addCarToDatabase(carItem)
        }
    }

    fun updateCar(carItem: CarItem) {
        CompletableFuture.runAsync {
            dao.getCarDatabaseDAO().updateCar(carItem)
        }
    }

}