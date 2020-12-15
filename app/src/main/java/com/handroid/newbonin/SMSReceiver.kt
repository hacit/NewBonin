package com.handroid.newbonin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SMSReceiver : BroadcastReceiver() {
    private lateinit var database: DatabaseReference

    val TAG = "SMSReceiver"

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "########onReceive");
        if (intent != null && intent.action != null && intent.action!!.equals("android.provider.Telephony.SMS_RECEIVED", ignoreCase = true)) {
            val bundle = intent.extras
            if (bundle != null) {
                val sms = bundle.get(SMS_BUNDLE) as Array<Any>?
                val smsMsg = StringBuilder()

                var smsMessage: SmsMessage
                if (sms != null) {
                    for (sm in sms) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            val format = bundle.getString("format")
                            smsMessage = SmsMessage.createFromPdu(sm as ByteArray, format)
                        } else {
                            smsMessage = SmsMessage.createFromPdu(sm as ByteArray)
                        }

                        val msgBody = smsMessage.messageBody.toString()
                        val msgAddress = smsMessage.originatingAddress
                        val msgMillis = smsMessage.timestampMillis
                        val date = Date(msgMillis)
                        val formatDate = SimpleDateFormat("yyyyMMdd HH:mm:ss")
                        val formatDay = SimpleDateFormat("yyyyMMdd")
                        val formatTime = SimpleDateFormat("HH:mm:ss")
                        val msgDate = formatDate.format(date)
                        val msgDay = formatDay.format(date)
                        val msgTime = formatTime.format(date)

                        smsMsg.append("SMS from : ").append(msgAddress).append("\n")
                        smsMsg.append(msgBody).append("\n")

                        Log.d(TAG, "########"+msgBody.toString());
                        Log.d(TAG, "########"+msgAddress.toString());
                        Log.d(TAG, "########"+msgDate.toString());
                        Log.d(TAG, "########"+msgDay.toString());
                        Log.d(TAG, "########"+msgTime.toString());

                        setFirebase(msgBody.toString(), msgAddress.toString(), msgDate.toString(), msgDay.toString(), msgTime.toString())
                    }
                }
            }
        }
    }


    private fun sendBroadcast(smsMSG: String) {
        val broadcastIntent = Intent()
//        broadcastIntent.action = AppConstants.mBroadcastSMSUpdateAction
//        broadcastIntent.putExtra(AppConstants.message, smsMSG)
//        EventBus.getDefault().post(EventIntent(broadcastIntent))
    }

    private fun setFirebase(body:String, address:String, date:String, day:String, time:String){
        database = Firebase.database.reference
        database.child(day).child(time).setValue(body)
    }

    companion object {

        val SMS_BUNDLE = "pdus"
    }

}