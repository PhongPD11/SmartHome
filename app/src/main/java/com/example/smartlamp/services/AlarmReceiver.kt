package com.example.smartlamp.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.smartlamp.R

const val notificationID = 1
const val channelID = "alarm_Id"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class AlarmReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        val message = intent.getStringExtra(messageExtra).toString()
        val title = intent.getStringExtra(titleExtra).toString()
        val notification =
            NotificationCompat.Builder(context, channelID).setSmallIcon(R.drawable.logo_32)
                .setContentText(message).setContentTitle(title).build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }

}