package ge.nsakandelidze.alarmApp

data class AlarmEntity(val id: String, val time: String, val isActive: Boolean){
    override fun toString(): String {
        return "$id|$time|False"
    }
}
