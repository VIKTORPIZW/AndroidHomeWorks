package by.it.academy.app8_2task.repositories

import android.content.Context
import by.it.academy.app8_2task.database.DatabaseCars
import by.it.academy.app8_2task.entity.WorkItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatabaseWorksRepository(context: Context) {

    private val dao = DatabaseCars.init(context)

    suspend fun getCarWorkList(carPlate: String) = withContext(Dispatchers.IO) {
        dao.getWorkListDatabaseDAO().getCarWorkList(carPlate)
    }

    suspend fun addWork(workItem: WorkItem) {
        withContext(Dispatchers.IO) { dao.getWorkListDatabaseDAO().addWork(workItem) }
    }

    suspend fun updateWork(workItem: WorkItem) {
        withContext(Dispatchers.IO) { dao.getWorkListDatabaseDAO().updateWork(workItem) }
    }

    suspend fun deleteWork(workItem: WorkItem) {
        withContext(Dispatchers.IO) { dao.getWorkListDatabaseDAO().deleteWork(workItem) }
    }

    suspend fun getFilteredCarWorkListByWorkType(carPlate: String, workStatus: Int) = withContext(Dispatchers.IO) {
        dao.getWorkListDatabaseDAO().getFilteredCarWorkListByWorkType(carPlate, workStatus)
    }
}