package com.handroid.newbonin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ms = Intent(this, MyService::class.java)
        var btn = findViewById<Button>(R.id.main_view_button)
        var btn_firebase = findViewById<Button>(R.id.btn_firebase)
        var tv_day = findViewById<TextView>(R.id.tv_day)
        var tv_total = findViewById<TextView>(R.id.tv_total)

        requirePerms()      //권한체크

        btn.setOnClickListener {
            startService(ms)    //Service 실행
        }
        btn_firebase.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val day = current.format(formatter)

            var database: DatabaseReference = Firebase.database.reference

            database.child(day).ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val RDvalue = dataSnapshot.getValue<HashMap<String,String>>()
                    var sb = StringBuilder()
                    for (key in RDvalue!!.keys){
                        sb.append(key).append(" = ")
                        sb.append(RDvalue[key].toString()).append("\n")
                    }
                    tv_day.setText("오늘은 ${day}")
                    tv_total.setText(sb.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    private fun requirePerms() {    //권한체크
        val permissions =
            arrayOf<String>(Manifest.permission.RECEIVE_SMS)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}