package com.example.homework6_1

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.it.academy.app6_1task.R

class MainActivity : AppCompatActivity() {

    private val workList = mutableListOf<WorkItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var workItemAdapter: WorkItemAdapter
    private lateinit var mainText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_works_list)

        mainText = findViewById(R.id.emptyWorkList_text)

        recyclerView = findViewById(R.id.workListRecycler)
        workItemAdapter = WorkItemAdapter(workList)
        recyclerView.adapter = workItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val cursor = contentResolver.query(
                Uri.parse("content://com.example.homework7_rxjava/database/works"),null,null,null, null
        )
        cursor?.run {
            cursor.moveToFirst()
            val work = WorkItem(
                    workStatus = cursor.getString(cursor.getColumnIndex("work_status")),
                    workName = cursor.getString(cursor.getColumnIndex("work_name")),
                    workDate = cursor.getString(cursor.getColumnIndex("work_date")),
                    workCost = cursor.getString(cursor.getColumnIndex("work_cost")),
            )
            workList.add(work)
            if (workList.isNotEmpty()){
                mainText.text = ""
            }
            while (cursor.moveToNext()){
            val works = WorkItem(
                    workStatus = cursor.getString(cursor.getColumnIndex("work_status")),
                    workName = cursor.getString(cursor.getColumnIndex("work_name")),
                    workDate = cursor.getString(cursor.getColumnIndex("work_date")),
                    workCost = cursor.getString(cursor.getColumnIndex("work_cost")),
            )
                workList.add(works)
            }
            workItemAdapter.notifyDataSetChanged()
            close()
        }
    }
}