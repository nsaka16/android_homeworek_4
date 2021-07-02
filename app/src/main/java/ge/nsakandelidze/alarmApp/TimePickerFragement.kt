package ge.nsakandelidze.alarmApp.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import ge.nsakandelidze.alarmApp.AlarmEntity
import java.util.*

class TimePickerFragment(
    val newAlarmConsumer: (AlarmEntity) -> Unit,
    val newNotificationConsumer: (AlarmEntity) -> Unit
) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val alarm = AlarmEntity(UUID.randomUUID().toString(), "$hourOfDay:$minute", true)
        newAlarmConsumer(alarm)
        newNotificationConsumer(alarm)
    }
}