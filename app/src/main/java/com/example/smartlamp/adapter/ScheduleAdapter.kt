package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemScheduleBinding
import com.example.smartlamp.model.ScheduleModel
import com.example.smartlamp.utils.RepeatDisplay

class ScheduleAdapter(
    var context: Context,
    var schedules: List<ScheduleModel>,
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
        var switch: Boolean

        bind.tvTime.text = "${item.hour}:${item.min}"

        val timeDisplay = RepeatDisplay.repeatDisplay(item.repeat)
        bind.tvRepeat.text = timeDisplay

        switch = (item.button == 1)
        bind.swDevice.isChecked = switch

        bind.swDevice.setOnCheckedChangeListener { _, isChecked ->
            onSwitchClickInterface.onSwitchClick(position, isChecked)
            switch = isChecked
        }

        holder.itemView.setOnClickListener {
            onScheduleClickInterface.onScheduleClick(item, timeDisplay, switch, position)
        }
    }

    override fun getItemCount(): Int {
        return schedules.size
    }

    interface SwitchClickInterface {
        fun onSwitchClick(position: Int, isChecked: Boolean)
    }

    interface ScheduleClickInterface {
        fun onScheduleClick(schedule: ScheduleModel, repeat: String, switch: Boolean, position: Int)
    }
}