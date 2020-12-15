package com.handroid.newbonin

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requirePerms()

        val ms = Intent(this, MyService::class.java)

        var btn = findViewById<Button>(R.id.main_view_button)

        btn.setOnClickListener {
//            val br: BroadcastReceiver = SMSReceiver()
//            val filter = IntentFilter()
//            filter.addAction("android.provider.Telephony.SMS_RECEIVED")
//            this.registerReceiver(br, filter)
            startService(ms)
        }

    }

    private fun requirePerms() {
        val permissions =
            arrayOf<String>(Manifest.permission.RECEIVE_SMS)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }
}