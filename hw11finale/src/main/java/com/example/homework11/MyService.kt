package com.example.homework11

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

const val BROADCAST_KEY = "BROADCAST_KEY"

class MyService: Service() {

    private var finalData: String? = null
    private val binder = MyServiceBinder()
    private val listType = object : TypeToken<List<LogItem>?>() {}.type
    private val gson = Gson()
    private var file: File? = null
    private var bool = false

    override fun onCreate() {
        super.onCreate()
        file = File(filesDir, "log.txt")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showServiceNotification()
        bool = true
        val data = intent?.getStringExtra(BROADCAST_KEY)
        val date = SimpleDateFormat("yyyy/M/dd", Locale.ROOT).format(Date())
        val time = SimpleDateFormat("HH:mm", Locale.ROOT).format(Date())
        val logItem = LogItem(time, date, data)
        if (file?.exists() == true){
            readAndWrite(logItem)
        } else{
            val target = mutableListOf<LogItem>()
            target.add(logItem)
            finalData = gson.toJson(target,listType)
            writeToFile(finalData ?: "empty data")
        }
        return START_STICKY
    }

    private fun readAndWrite(logItem: LogItem){
        val reader = FileReader(file)
        Single.create<List<LogItem>> { emmiter ->
            val existingJson = gson.fromJson<List<LogItem>>(JsonReader(reader),listType)
            val newJson = existingJson.plus(logItem)
            emmiter.onSuccess(newJson)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { newLogs ->
                    val data = gson.toJson(newLogs,listType)
                    writeToFile(data)
                }
    }

    private fun writeToFile(data: String){
        Single.create<String> { emmiter ->
            file?.delete()
            file?.appendText(data)
            emmiter.onSuccess(data)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun showServiceNotification() {
        val channel = createNotificationChannel("ID_CHANNEL", "NAME_CHANNEL")
        val notification = NotificationCompat.Builder(baseContext, channel)
                .setSmallIcon(R.drawable.ic_baseline_miscellaneous_services_24)
                .setContentTitle("My Service is running")
                .setContentText("The service is working in the background")
                .build()
        startForeground(5, notification)
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class MyServiceBinder: Binder(){
        fun getData() = bool
        fun setBoolFalse(){
            bool = false
        }
    }
}