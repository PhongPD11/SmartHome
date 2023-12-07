package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemCustomChooseBinding
import com.example.smartlamp.model.CustomChooseModel


class CustomChooseAdapter(
    var context: Context,
    private var days: ArrayList<CustomChooseModel>,
    private val onDayClickInterface: DayClickInterface,
) : RecyclerView.Adapter<CustomChooseAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemCustomChooseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCustomChooseBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = days[position]
        val bind = holder.binding

        bind.tvDay.text = item.day

        if (item.state == '1') {
            bind.ivCheck.setImageResource(R.drawable.ic_check_cir)
        }
        else {
            bind.ivCheck.setImageResource(R.drawable.ic_cir)
        }

        holder.itemView.setOnClickListener {
            onDayClickInterface.onDayClick(position)
        }

    }


    override fun getItemCount(): Int {
        return days.size
    }

    interface DayClickInterface {
        fun onDayClick(position: Int)
    }

}