package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemAlarmBinding
import com.example.smartlamp.databinding.ItemRoomBinding
import com.example.smartlamp.model.AlarmModel
import com.example.smartlamp.model.RoomModel

class AlarmAdapter(
    var context: Context,
    var alarms: List<AlarmModel>,
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

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
        var bind = holder.binding
        var timeDisplay = ""

        bind.tvTime.text = item.time
        for (i in 0 until  item.repeat.size){
            timeDisplay += "${item.repeat[i]}, "
        }
        bind.tvRepeat.text = timeDisplay
    }


    override fun getItemCount(): Int {
        return alarms.size
    }

}