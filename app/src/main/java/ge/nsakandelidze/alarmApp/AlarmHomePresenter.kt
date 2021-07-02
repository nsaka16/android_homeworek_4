package ge.nsakandelidze.alarmApp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import java.util.*

class AlarmHomePresenter(
    private var view: IAlarmHomeView,
    private var newNotificationConsumer: (AlarmEntity) -> Unit
) : BroadcastReceiver() {

    private val CANCEL_EVENT_ACTION = "CANCEL_ACTION"
    private val SNOOZE_EVENT_ACTION = "SNOOZE_ACTION"

    private val storage: Dao = Dao.getInstance()

    fun updateAlarmsData() {
        view.showListOfAlarms(storage.getAllAlarms())
    }

    fun addNewAlarm(alarmEntity: AlarmEntity) {
        storage.addNewAlarm(alarmEntity)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val tempIntent = Intent()
        Log.d("temp", tempIntent.toString())
        val from = context?.let { NotificationManagerCompat.from(it) }
        val action = intent?.action
        val intExtra = intent?.getIntExtra("notificationId", 73);
        from?.cancel(intExtra!!)
        if (action.equals(CANCEL_EVENT_ACTION)) {
            //cancel event handle
        } else if (action.equals(SNOOZE_EVENT_ACTION)) {
            val id = intent?.getStringExtra("id");
            val alarmEntityWithIdOf: AlarmEntity? = storage.getAlarmWithIdOf(id.orEmpty())
            Log.d("s",alarmEntityWithIdOf.toString())
            alarmEntityWithIdOf?.let {
                val calendar = getCalendar(alarmEntityWithIdOf)
                calendar.add(Calendar.MINUTE, 1)
                val hour = calendar.get(Calendar.HOUR)
                val minute = calendar.get(Calendar.MINUTE)
                val res ="$hour:$minute"
                val alarm =
                    AlarmEntity(UUID.randomUUID().toString(), res, false)
                newNotificationConsumer(alarm)
                addNewAlarm(alarm)
                updateAlarmsData()
                Log.d("a", it.toString())
            }
        }
    }

    fun getCalendar(alarmEntity: AlarmEntity): Calendar {
        val cal: Calendar = Calendar.getInstance()
        val split = alarmEntity.time.split(":")
        cal.set(Calendar.HOUR_OF_DAY, split[0].toInt())
        cal.set(Calendar.MINUTE, split[1].toInt())
        return cal;
    }
}