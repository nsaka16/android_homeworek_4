package ge.nsakandelidze.alarmApp

import android.content.Context
import android.content.SharedPreferences

class Dao {
    private lateinit var sharedPreferences: SharedPreferences

    private val PREFERENCES_IDS_DATA_KEY = "id_keys"

    fun getAllAlarms(): List<AlarmEntity> {
        val splitted = sharedPreferences.getStringSet(PREFERENCES_IDS_DATA_KEY, mutableSetOf())
        return if (splitted!!.isEmpty()) {
            listOf()
        } else {
            splitted
                .filter { sharedPreferences.getString(it, "")!!.split("|").isNotEmpty() }
                .map {
                    val alarmData = sharedPreferences.getString(it, "")
                    val dataArray = alarmData!!.split("|")
                    AlarmEntity(dataArray[0], dataArray[1], dataArray[2] == "True")
                }
        }
    }


    fun addNewAlarm(alarmEntity: AlarmEntity) {
        with(sharedPreferences.edit()) {
            putString(alarmEntity.id, alarmEntity.toString())
            val stringSet = sharedPreferences.getStringSet(PREFERENCES_IDS_DATA_KEY, mutableSetOf())
            stringSet!!.add(alarmEntity.id)
            putStringSet(PREFERENCES_IDS_DATA_KEY, stringSet)
            commit()
        }
    }

    fun getAlarmWithIdOf(alarmId: String): AlarmEntity? {
        val splitted = sharedPreferences.getString(alarmId, "")
        if (splitted.equals("")) {
            return null
        } else {
            val dataArray = splitted!!.split("|")
            return AlarmEntity(dataArray[0], dataArray[1], dataArray[2] == "True")
        }
    }

    companion object {
        private lateinit var INSTANCE: Dao

        fun createDb(context: Context) {
            INSTANCE = Dao()
            INSTANCE.sharedPreferences =
                context.getSharedPreferences("storage", Context.MODE_PRIVATE)
        }

        fun getInstance(): Dao {
            return INSTANCE
        }
    }
}