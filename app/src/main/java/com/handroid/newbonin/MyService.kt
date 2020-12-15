package com.handroid.newbonin

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {
    //Service
    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet Implemented");
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
