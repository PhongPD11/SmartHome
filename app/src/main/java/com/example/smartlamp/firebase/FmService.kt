package com.example.smartlamp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smartlamp.R
import com.example.smartlamp.activity.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FmService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            if (remoteMessage.data.isNotEmpty()) {
                val data = remoteMessage.data
                val title = data["title"]
                val message = data["message"]

                sendNotification(title, message)
                sendBroadcastToActivity()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun sendBroadcastToActivity() {
        val intent = Intent("custom-event")
        intent.putExtra("isReload", true)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendNotification(title: String?, message: String?) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "Firebase_notification_ID"
        val channelName = "Smart_library_notification"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.logo_32)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, notificationBuilder.build())
    }
}