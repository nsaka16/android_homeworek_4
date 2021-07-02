package ge.nsakandelidze.alarmApp.reciever

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import ge.nsakandelidze.alarmApp.R

class AlarmNotificationReciever : BroadcastReceiver() {
    private val CANCEL_EVENT_ACTION = "CANCEL_ACTION"
    private val SNOOZE_EVENT_ACTION = "SNOOZE_ACTION"
    private val NOTIFICATION_ID: Int = 73


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val from = context?.let { NotificationManagerCompat.from(it) }

        val cancelIntent = Intent(CANCEL_EVENT_ACTION)
        cancelIntent.putExtra("id", intent?.getStringExtra("id"))
        cancelIntent.putExtra("notificationId", 73)

        val snoozeIntent = Intent(SNOOZE_EVENT_ACTION)
        snoozeIntent.putExtra("id", intent?.getStringExtra("id"))
        snoozeIntent.putExtra("notificationId", 73)

        val notificationCancelIntent =
            PendingIntent.getBroadcast(context, 0, cancelIntent, 0)

        val notificationSnoozeIntent =
            PendingIntent.getBroadcast(context, 0, snoozeIntent, 0)

        from?.createNotificationChannel(
            NotificationChannel(
                "channel_id_1",
                "my channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        val time: String = intent?.getStringExtra("time").orEmpty()
        from?.notify(
            NOTIFICATION_ID, Notification.Builder(context, "channel_id_1")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("message!")
                .setContentText("Alarm message body")
                .addAction(R.mipmap.ic_launcher, "Cancel", notificationCancelIntent)
                .addAction(R.mipmap.ic_launcher, "Snooze", notificationSnoozeIntent)
                .build(),
        )
    }

}