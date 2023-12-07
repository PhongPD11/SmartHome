package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemScheduleBinding
import com.example.smartlamp.model.ScheduleModel
import com.example.smartlamp.model.ScheduleResponse
import com.example.smartlamp.utils.Utils

class ScheduleAdapter(
    var context: Context,
    var schedules: List<ScheduleResponse.ScheduleData>,
    private val onSwitchClickInterface: ScheduleAdapter.SwitchClickInterface,
    private val onScheduleClickInterface: ScheduleAdapter.ScheduleClickInterface,
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemScheduleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemScheduleBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = schedules[position]
        val bind = holder.binding
        var switch = item.isOn

        bind.tvTime.text = Utils.updateTime(item.hourTime,item.minuteTime)

        if (item.typeRepeat == "Custom") {
            val timeDisplay = Utils.repeatDisplay(item.repeat)
            bind.tvRepeat.text = timeDisplay
        } else {
            bind.tvRepeat.text = item.typeRepeat
        }
        bind.swDevice.isChecked = switch

        bind.swDevice.setOnCheckedChangeListener { _, isChecked ->
            onSwitchClickInterface.onSwitchClick(position, isChecked)
            switch = isChecked
        }

        holder.itemView.setOnClickListener {
            onScheduleClickInterface.onScheduleClick(position)
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    interface SwitchClickInterface {
        fun onSwitchClick(position: Int, isChecked: Boolean)
    }

    interface ScheduleClickInterface {
        fun onScheduleClick(position: Int)
    }
}