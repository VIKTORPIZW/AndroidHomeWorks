package by.it.academy.app6_1task

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarWorkAdapter
    private val values: MutableList<WorkItem> = mutableListOf()

    private val URI_PATH = "content://by.it.academy.app5task.app5task.content_provider.CarWorksContentProvider/WorksDataBase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getWorks()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter = CarWorkAdapter(values)
        recyclerView.adapter = adapter
    }

    private fun getWorks() {
        val cursor: Cursor? = contentResolver.query(Uri.parse(URI_PATH), null, null, null, null, null)

        cursor?.run {
            while (moveToNext()) {
                values.add(WorkItem(
                        getString(getColumnIndex("applicationDate")),
                        getString(getColumnIndex("workType")),
                        getFloat(getColumnIndex("cost")),
                        getInt(getColumnIndex("workStatus"))))
            }
        }
        cursor?.close()
    }


}