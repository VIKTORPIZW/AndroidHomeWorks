package by.it.academy.app7_completablefuture.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import by.it.academy.app7_completablefuture.database.DatabaseCars
import by.it.academy.app7_completablefuture.entity.WorkItem
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class DatabaseWorksRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    fun getCarWorkList(carPlate: String): CompletableFuture<MutableList<WorkItem>> = CompletableFuture.supplyAsync {
        return@supplyAsync dao.getWorkListDatabaseDAO().getCarWorkList(carPlate)
    }

    fun addWork(workItem: WorkItem) {
        CompletableFuture.runAsync {
            dao.getWorkListDatabaseDAO().addWork(workItem)
        }
    }

    fun updateWork(workItem: WorkItem) {
        CompletableFuture.runAsync {
            dao.getWorkListDatabaseDAO().updateWork(workItem)
        }
    }

    fun deleteWork(workItem: WorkItem) {
        CompletableFuture.runAsync {
            dao.getWorkListDatabaseDAO().deleteWork(workItem)
        }
    }

    fun getFilteredCarWorkListByWorkType(carPlate: String,
                                         workStatus: Int): CompletableFuture<MutableList<WorkItem>>? = CompletableFuture.supplyAsync {
        return@supplyAsync dao.getWorkListDatabaseDAO().getFilteredCarWorkListByWorkType(carPlate, workStatus)
    }

}