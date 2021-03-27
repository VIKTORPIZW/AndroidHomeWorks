package by.it.academy.app7_executor_service.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import by.it.academy.app7_executor_service.database.DatabaseCars
import by.it.academy.app7_executor_service.entity.CarItem
import java.util.concurrent.Executors

class DatabaseCarsRepository(context: Context) {

    private val dao = DatabaseCars.init(context)
    private val executorService = Executors.newSingleThreadExecutor()
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    fun getAllCarsSortedByProducer(callbackListener: (MutableList<CarItem>) -> Unit) {
        executorService.submit<MutableList<CarItem>> {
            dao.getCarDatabaseDAO().getAllCarsSorted().apply {
                mainExecutor.execute {
                    callbackListener.invoke(this)
                }
            }
        }
    }

    fun addCar(carItem :CarItem){
        executorService.submit {
            dao.getCarDatabaseDAO().addCarToDatabase(carItem)
        }
    }

    fun updateCar(carItem: CarItem){
        executorService.submit {
            dao.getCarDatabaseDAO().updateCar(carItem)
        }
    }
}