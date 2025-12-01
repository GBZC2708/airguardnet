package com.airguardnet.mobile.core.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.airguardnet.mobile.MainActivity
import com.airguardnet.mobile.R

object NotificationHelper {
    const val SESSION_CHANNEL_ID = "session_channel"
    private const val SESSION_NOTIFICATION_ID = 2010

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = NotificationManagerCompat.from(context)
            val sessionChannel = NotificationChannel(
                SESSION_CHANNEL_ID,
                "Sesiones AirGuardNet",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(sessionChannel)
        }
    }

    fun showSessionNotification(context: Context, role: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission =
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) return
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("fromNotification", true)
            putExtra("targetTab", "home")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            SESSION_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SESSION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Sesión iniciada")
            .setContentText("Has iniciado sesión como $role en AirGuardNet.")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(SESSION_NOTIFICATION_ID, notification)
    }
}
