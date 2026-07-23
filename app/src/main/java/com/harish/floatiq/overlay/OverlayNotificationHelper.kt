package com.harish.floatiq.overlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object OverlayNotificationHelper {

    private const val CHANNEL_ID =
        "floatiq_overlay"

    fun createNotification(
        context: Context
    ): Notification {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "FloatIQ Overlay",
                    NotificationManager.IMPORTANCE_LOW
                )

            val manager =
                context.getSystemService(
                    NotificationManager::class.java
                )

            manager.createNotificationChannel(
                channel
            )
        }

        return Notification.Builder(
            context,
            CHANNEL_ID
        )
            .setContentTitle("FloatIQ")
            .setContentText(
                "Floating Assistant Running"
            )
            .setSmallIcon(
                android.R.drawable.ic_dialog_info
            )
            .build()
    }
}