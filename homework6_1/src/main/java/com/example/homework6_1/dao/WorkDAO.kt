package com.example.homework6_1.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.homework6_1.data.Work

@Dao
interface WorkDAO {
    @Query ("SELECT * FROM work_item")
    fun getWorkList(): List<Work>

    @Query("SELECT * FROM work_item WHERE id = :workId")
    fun getWork(workId: Int): Work

    @Query("SELECT * FROM work_item WHERE parentCar LIKE :parentCar")
    fun getWorkFromParentsCar(parentCar: String?): List<Work>

    @Query("SELECT * FROm work_item")
    fun getWorkProvider(): Cursor

    @Query("DELETE FROM work_item")
    fun deleteAllList()

    @Insert
    fun insertAll(work: Work)

    @Update
    fun update(work: Work)

    @Delete
    fun delete(work: Work)
}