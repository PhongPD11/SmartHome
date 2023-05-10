package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemAlarmBinding
import com.example.smartlamp.databinding.ItemRoomBinding
import com.example.smartlamp.fragment.AlarmFragment
import com.example.smartlamp.model.AlarmModel
import com.example.smartlamp.model.DeviceModel
import com.example.smartlamp.model.RoomModel

class AlarmAdapter(
    var context: Context,
    var alarms: List<AlarmModel>,
    private val onSwitchClickInterface: AlarmAdapter.SwitchClickInterface,
    private val onAlarmClickInterface: AlarmAdapter.AlarmClickInterface,
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    private val dotw = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    var everyDay = true
    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAlarmBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = alarms[position]
        val bind = holder.binding
        var timeDisplay = ""

        bind.tvTime.text = item.time
        if (item.repeat[7] == 1) {
            timeDisplay = "Once Time"
            everyDay = false
        } else {
            for (i in 0 until item.repeat.size - 1) {
                if (item.repeat[i] == 1) {
                    timeDisplay += "${dotw[i]}, "
                } else {
                    everyDay = false
                }
            }
        }
        if (everyDay) {
            timeDisplay = "Everyday"
        }
        bind.tvRepeat.text = timeDisplay

        bind.swDevice.setOnCheckedChangeListener { _, isChecked ->
            onSwitchClickInterface.onSwitchClick(position, isChecked)
        }

        holder.itemView.setOnClickListener {
            onAlarmClickInterface.onAlarmClick(item)
        }
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    interface SwitchClickInterface {
        fun onSwitchClick(position: Int, isChecked: Boolean)
    }

    interface AlarmClickInterface {
        fun onAlarmClick(alarm: AlarmModel)
    }
}