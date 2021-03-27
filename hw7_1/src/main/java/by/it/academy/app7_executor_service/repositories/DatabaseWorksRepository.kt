package by.it.academy.app7_executor_service.repositories

import android.content.Context
import androidx.core.content.ContextCompat
import by.it.academy.app7_executor_service.database.DatabaseCars
import by.it.academy.app7_executor_service.entity.WorkItem
import java.util.concurrent.Executors

class DatabaseWorksRepository(context: Context) {

    private val dao = DatabaseCars.init(context)
    private val executorService = Executors.newSingleThreadExecutor()
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    fun getCarWorkList(carPlate:String, callbackListener: (MutableList<WorkItem>) -> Unit) {
        executorService.submit<MutableList<WorkItem>>{
            dao.getWorkListDatabaseDAO().getCarWorkList(carPlate).apply{
                mainExecutor.execute{
                    callbackListener.invoke(this)
                }
            }
        }
    }

    fun addWork(workItem: WorkItem){
        executorService.submit{
            dao.getWorkListDatabaseDAO().addWork(workItem)
        }
    }

    fun updateWork(workItem: WorkItem){
        executorService.submit{
            dao.getWorkListDatabaseDAO().updateWork(workItem)
        }
    }

    fun deleteWork(workItem: WorkItem){
        executorService.submit{
            dao.getWorkListDatabaseDAO().deleteWork(workItem)
        }
    }

    fun getFilteredCarWorkListByWorkType(carPlate: String,
                                         workStatus: Int,
                                         callbackListener: (MutableList<WorkItem>) -> Unit){
        executorService.submit<MutableList<WorkItem>>{
            dao.getWorkListDatabaseDAO().getFilteredCarWorkListByWorkType(carPlate, workStatus).apply{
                mainExecutor.execute{
                    callbackListener.invoke(this)
                }
            }
        }
    }

}