package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemRoomBinding
import com.example.smartlamp.model.RoomModel
import java.util.ArrayList

class RoomAdapter(
    var context: Context,
    var rooms: List<RoomModel>,
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
        val bind = holder.binding

        holder.binding.ivRoom.setImageResource(item.image)
        holder.binding.tvRoom.text = item.room
    }


    override fun getItemCount(): Int {
        return rooms.size
    }

}