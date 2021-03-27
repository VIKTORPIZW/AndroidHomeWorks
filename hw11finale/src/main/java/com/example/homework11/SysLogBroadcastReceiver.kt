package com.example.homework11

import android.content.*
import android.os.IBinder
import android.util.Log
import androidx.core.content.getSystemService

class SysLogBroadcastReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, MyService::class.java).apply {
            putExtra(BROADCAST_KEY, intent?.action.toString())
        }
            context?.startForegroundService(serviceIntent)
    }
}