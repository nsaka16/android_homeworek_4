package ge.nsakandelidze.alarmApp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.nsakandelidze.alarmApp.R
import ge.nsakandelidze.alarmApp.AlarmEntity

class AlarmListAdapter(val alarmEntities: List<AlarmEntity>) :
    RecyclerView.Adapter<AlarmItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItem {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_item_layout, parent, false)
        val alarmItem = AlarmItem(view)
        return alarmItem
    }

    override fun onBindViewHolder(holder: AlarmItem, position: Int) {
        val todoItem = alarmEntities[position]
        holder.alarmTime.text = todoItem.time
        holder.alarmActiveSwitch.isChecked = todoItem.isActive
        holder.id = todoItem.id
    }

    override fun getItemCount(): Int {
        return alarmEntities.size
    }
}

class AlarmItem(var itemView: View) : RecyclerView.ViewHolder(itemView) {
    var alarmTime = itemView.findViewById<TextView>(R.id.alarm_time_textview_id)
    var alarmActiveSwitch = itemView.findViewById<Switch>(R.id.alarm_active_switch)
    var id: String = ""
}