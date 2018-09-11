package com.softices.kotlintraineeapp.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Toast
import java.util.*


class MyService : Service() {
    var timerObj: Timer? = null
    /**
     * using this method you can bind activity with this service.
     */
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    /**
     *execute when start service call.
     */
    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        /**
         *  toast after every 2 sec.
         */
        timerObj = Timer()
        val timerTaskObj = object : TimerTask() {
            override fun run() {
                val msg = Message()
                msg.obj = "yeh"
                responseHandler.sendMessage(msg);
            }
        }
        timerObj!!.schedule(timerTaskObj, 2000, 5000)
    }

    // on start command
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    // handle for execute for toast
    val responseHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Toast.makeText(applicationContext, "yeh!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * service destroy
     */
    override fun onDestroy() {
        timerObj!!.cancel()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun stopService(name: Intent): Boolean {
        timerObj!!.cancel()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
        return super.stopService(name)
    }
}