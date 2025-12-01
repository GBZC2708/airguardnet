package com.airguardnet.mobile.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.airguardnet.mobile.MainActivity
import com.airguardnet.mobile.R

object NotificationHelper {

    private const val SESSION_CHANNEL_ID = "session_channel"
    private const val SESSION_CHANNEL_NAME = "Sesiones AirGuardNet"
    private const val SESSION_CHANNEL_DESC = "Notificaciones cuando se inicia sesión"
    private const val SESSION_NOTIFICATION_ID = 2010

    private const val CRITICAL_ALERT_CHANNEL_ID = "critical_alerts_channel"
    private const val CRITICAL_ALERT_CHANNEL_NAME = "Alertas críticas"
    private const val CRITICAL_ALERT_CHANNEL_DESC = "Notificaciones de alertas críticas de polvo"

    /**
     * Se llama UNA VEZ al inicio de la app (AirGuardNetApp.onCreate)
     * para registrar el canal de notificaciones de sesión.
     */
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val existing = manager.getNotificationChannel(SESSION_CHANNEL_ID)
            if (existing == null) {
                val sessionChannel = NotificationChannel(
                    SESSION_CHANNEL_ID,
                    SESSION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = SESSION_CHANNEL_DESC
                }
                manager.createNotificationChannel(sessionChannel)
            }

            val criticalChannel = manager.getNotificationChannel(CRITICAL_ALERT_CHANNEL_ID)
            if (criticalChannel == null) {
                val channel = NotificationChannel(
                    CRITICAL_ALERT_CHANNEL_ID,
                    CRITICAL_ALERT_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = CRITICAL_ALERT_CHANNEL_DESC
                    enableVibration(true)
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    /**
     * Muestra la notificación de "Sesión iniciada".
     * El permiso POST_NOTIFICATIONS ya lo maneja AuthScreen.
     */
    fun showSessionNotification(context: Context, role: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("fromNotification", true)
            putExtra("targetTab", "home")
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else
                        0

        val pendingIntent = PendingIntent.getActivity(
            context,
            SESSION_NOTIFICATION_ID,
            intent,
            pendingIntentFlags
        )

        val notification = NotificationCompat.Builder(context, SESSION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // usa un ícono válido de tu app
            .setContentTitle("Sesión iniciada")
            .setContentText("Has iniciado sesión como $role en AirGuardNet.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(SESSION_NOTIFICATION_ID, notification)
    }

    fun showCriticalAlertNotification(
        context: Context,
        newCount: Int,
        deviceUid: String?,
        message: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("fromNotification", true)
            putExtra("targetTab", "alerts")
            putExtra("alertFilter", "CRITICAL")
        }

        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        PendingIntent.FLAG_IMMUTABLE
                    else
                        0

        val pendingIntent = PendingIntent.getActivity(
            context,
            CRITICAL_ALERT_CHANNEL_ID.hashCode(),
            intent,
            pendingIntentFlags
        )

        val title = if (newCount > 1) {
            "Tienes $newCount nuevas alertas críticas de polvo"
        } else {
            "Alerta crítica de polvo${deviceUid?.let { ": PM2.5 elevado en $it" } ?: ""}"
        }

        val notification = NotificationCompat.Builder(context, CRITICAL_ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(CRITICAL_ALERT_CHANNEL_ID.hashCode(), notification)
    }
}
