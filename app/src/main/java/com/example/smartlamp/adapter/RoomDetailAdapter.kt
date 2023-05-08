package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemDeviceBinding
import com.example.smartlamp.model.DeviceModel

class RoomDetailAdapter(
    var context: Context,
    var rooms: List<DeviceModel>,
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
        bind.swDevice.isChecked = item.button
        bind.slider.value = item.track
    }


    override fun getItemCount(): Int {
        return rooms.size
    }

}