package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemDeviceBinding
import com.example.smartlamp.model.DeviceModel


class RoomDetailAdapter(
    var context: Context,
    var rooms: List<DeviceModel>,
    private val onSwitchClickInterface: SwitchClickInterface,
    private val onDeviceClickInterface: DeviceClickInterface,
    private val onTrackChangeInterface: TrackChangeInterface,
    ) : RecyclerView.Adapter<RoomDetailAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDeviceBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rooms[position]
        val bind = holder.binding

        bind.ivDevice.setImageResource(item.image)
        bind.tvDevice.text = item.name
//        bind.swDevice.isChecked = item.button

        bind.swDevice.setOnCheckedChangeListener { _, isChecked ->
            onSwitchClickInterface.onSwitchClick(position, isChecked)
        }

        holder.itemView.setOnClickListener {
            onDeviceClickInterface.onDeviceClick(item)
        }

        bind.slider.addOnChangeListener { slider, value, fromUser ->
            onTrackChangeInterface.onTrackChange(value)
        }

        bind.slider.value = item.track

    }


    override fun getItemCount(): Int {
        return rooms.size
    }

    interface SwitchClickInterface {
        fun onSwitchClick(position: Int, isChecked: Boolean)
    }

    interface DeviceClickInterface {
        fun onDeviceClick(device: DeviceModel)
    }

    interface TrackChangeInterface {
        fun onTrackChange(value: Float)
    }

}