package ge.nsakandelidze.alarmApp

import android.app.Application

class Init : Application() {
    override fun onCreate() {
        super.onCreate()
        Dao.createDb(this)
    }
}