package by.it.academy.app7_executor_service.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.it.academy.app7_executor_service.entity.WorkItem

@Dao
interface WorkItemDAO {

    @Query("SELECT * FROM WorksDataBase WHERE carPlate = :carPlate")
    fun getCarWorkList(carPlate: String): MutableList<WorkItem>

    @Insert
    fun addWork(entity: WorkItem)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWork(workItem:WorkItem)

    @Delete
    fun deleteWork(workItem: WorkItem)

    @Query("SELECT * FROM WorksDataBase WHERE carPlate = :carPlate AND workStatus =:workStatus")
    fun getFilteredCarWorkListByWorkType(carPlate: String, workStatus:Int) :MutableList<WorkItem>

}