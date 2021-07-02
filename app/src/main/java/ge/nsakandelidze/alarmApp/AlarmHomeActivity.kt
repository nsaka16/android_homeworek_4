package ge.nsakandelidze.alarmApp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ge.nsakandelidze.alarmApp.adapter.AlarmListAdapter
import ge.nsakandelidze.alarmApp.fragments.TimePickerFragment
import java.util.*

class AlarmHomeActivity : AppCompatActivity(), IAlarmHomeView {
    private var listOfAlarmEntities: MutableList<AlarmEntity> = mutableListOf()
    private lateinit var presenter: AlarmHomePresenter

    private lateinit var alarmsListRecyclerView: RecyclerView
    private lateinit var addAlarmIcon: ImageView
    private lateinit var switchThemeText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_home_activity)
        alarmsListRecyclerView = findViewById(R.id.alarms_recycler_view_id)
        alarmsListRecyclerView.adapter = AlarmListAdapter(listOfAlarmEntities)
        alarmsListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        addAlarmIcon = findViewById(R.id.add_alarm_icon_id)
        switchThemeText = findViewById(R.id.switch_theme_view_id)
        addAlarmIcon.setOnClickListener {
            TimePickerFragment({
                presenter.addNewAlarm(it)
            }, {
                setAlarm(it)
            }).show(supportFragmentManager, "timePicker")
        }
        this.presenter = AlarmHomePresenter(this) { setAlarm(it) }
        val filter = IntentFilter()
        filter.addAction("CANCEL_ACTION")
        filter.addAction("SNOOZE_ACTION")
        registerReceiver(presenter, filter)
        this.presenter.updateAlarmsData()
        start()
    }

    private fun start() {
        presenter.updateAlarmsData()
    }

    override fun showListOfAlarms(alarmEntities: List<AlarmEntity>) {
        runOnUiThread {
            listOfAlarmEntities.clear()
            listOfAlarmEntities.addAll(alarmEntities)
            alarmsListRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun setAlarm(alarmEntity: AlarmEntity) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent("ALARM_MESSAGE_ACTION")
        intent.putExtra("time", alarmEntity.time)
        intent.putExtra("id", alarmEntity.id)
        val broadcast = PendingIntent.getBroadcast(this, 100, intent, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                getCalendar(alarmEntity).timeInMillis,
                broadcast
            )
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