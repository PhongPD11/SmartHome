package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemModeBinding
import com.example.smartlamp.model.ModeModel

class ModeAdapter(
    var context: Context,
    var modes: List<ModeModel>,
    private val onModeClickInterface: ModeClickInterface,
    ) : RecyclerView.Adapter<ModeAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemModeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemModeBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = modes[position]
        val bind = holder.binding

        if (item.modeOn){
            holder.itemView.alpha = 1f
        } else {
            holder.itemView.alpha = 0.5f
        }

        bind.ivMode.setImageResource(item.image)
        bind.tvMode.text = item.mode

        holder.itemView.setOnClickListener {
            onModeClickInterface.onModeClick(position)
        }
    }


    override fun getItemCount(): Int {
        return modes.size
    }

    interface ModeClickInterface {
        fun onModeClick(position: Int)
    }

}