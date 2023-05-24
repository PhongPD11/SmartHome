package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemRoomBinding
import com.example.smartlamp.model.DeviceModel
import com.example.smartlamp.model.RoomModel

class RoomAdapter(
    var context: Context,
    var rooms: List<RoomModel>,
    private val onRoomClickInterface: RoomClickInterface,
    ) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRoomBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rooms[position]

        holder.binding.ivRoom.setImageResource(item.image)
        holder.binding.tvRoom.text = item.room

        holder.itemView.setOnClickListener {
            onRoomClickInterface.onRoomClick(item)
        }
    }


    override fun getItemCount(): Int {
        return rooms.size
    }

    interface RoomClickInterface {
        fun onRoomClick(room: RoomModel)
    }

}