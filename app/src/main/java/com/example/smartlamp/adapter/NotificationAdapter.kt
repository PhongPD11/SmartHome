package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemNotificationBinding
import com.example.smartlamp.model.NotificationModel
import io.tux.wallet.testnet.utils.ConvertTime

class NotificationAdapter(
    var context: Context,
    var notifications: ArrayList<NotificationModel.Data>,
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notifications[position]
        val bind = holder.binding
        bind.tvTitle.text = item.title
        bind.tvContent.text = item.content
        bind.tvTime.text = ConvertTime.formatDate(item.createAt, "dd MMM yyyy - HH:mm:ss")
    }


    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
//        notifications.removeAt(position)
//        notifyItemRemoved(position)
//        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return notifications.size
    }

}
