package vn.geekup.app.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import vn.geekup.app.BuildConfig
import vn.geekup.app.R
import vn.geekup.app.module.root.RootActivity
import java.util.*

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        //Todo send Token to Server
        Timber.d("FCM Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Message data payload: ${remoteMessage.data}")
        val intent = Intent(this, RootActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        buildNotification(remoteMessage, pendingIntent)
    }

    private fun buildNotification(remoteMessage: RemoteMessage, pendingIntent: PendingIntent?) {
        val notificationNumber = Random().nextInt(99999 - 10001) + 1000

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = getString(R.string.default_notification_channel_id)

        handleNotificationAndroidO(notificationManager)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
            .setSmallIcon(R.drawable.ic_notification_active)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(this, R.color.color_accent))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationNumber, notification)
    }

    private fun handleNotificationAndroidO(notificationManager: NotificationManager) {
        createNotificationChannel(notificationManager)
        notificationManager
            .getNotificationChannel(BuildConfig.APPLICATION_ID)
            ?.canBypassDnd()
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel =
            NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.default_notification_channel_name),
                IMPORTANCE_HIGH
            )
        notificationManager.createNotificationChannel(notificationChannel)
    }
}