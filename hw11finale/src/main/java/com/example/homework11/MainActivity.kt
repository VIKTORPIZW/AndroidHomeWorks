package com.example.homework11

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileReader
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private var serviceBinder: MyService.MyServiceBinder? = null
    private var logsList = mutableListOf<LogItem>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var logItemAdapter: LogItemAdapter
    private var newLog: Boolean = false
    private var file: File? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = service as MyService.MyServiceBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar? = findViewById(R.id.logs_toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recycler_logs)
        logItemAdapter = LogItemAdapter(logsList)
        recyclerView.adapter = logItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        file = File(filesDir, "log.txt")
        if (file?.exists() == true){
            getDataFromFile(file)
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MyService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        updateList()
    }

    private fun updateList(){
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
            newLog = serviceBinder?.getData() == true
            if (newLog && file?.exists() == true){
                getDataFromFile(file)
                serviceBinder?.setBoolFalse()
            }
        }, 1, 1, TimeUnit.SECONDS)
    }

    private fun getDataFromFile(file: File?){
        val gson = Gson()
        val reader = FileReader(file)
        val listType = object : TypeToken<List<LogItem>?>() {}.type
        Single.create<List<LogItem>> { emmiter ->
            val existingJson = gson.fromJson<List<LogItem>>(JsonReader(reader), listType)
            emmiter.onSuccess(existingJson)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { logs ->
                    logsList.clear()
                    logsList.addAll(logs)
                    logItemAdapter.notifyDataSetChanged()
                }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_date -> {
                logsList.sortBy { it.date }
                logItemAdapter.notifyDataSetChanged()
            }
            R.id.sort_time -> {
                logsList.sortBy { it.time }
                logItemAdapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}