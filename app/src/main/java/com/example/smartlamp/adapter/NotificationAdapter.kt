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
import com.example.smartlamp.model.NotificationType
import com.example.smartlamp.utils.ConvertTime

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



    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = notifications[position]
        val bind = holder.binding
        bind.tvTitle.text = item.title
        bind.tvContent.text = item.content
        bind.tvTime.text = ConvertTime.formatDate(item.createAt, "dd MMM yyyy - HH:mm")
        when (item.type) {
            NotificationType.ALERT.status -> {
                bind.ivType.setImageResource(NotificationType.ALERT.imageResource)
            }
            NotificationType.WARNING.status -> {
                bind.ivType.setImageResource(NotificationType.WARNING.imageResource)
            }
            NotificationType.VOLUNTEER.status -> {
                bind.ivType.setImageResource(NotificationType.VOLUNTEER.imageResource)
            }
            NotificationType.CONGRATS.status -> {
                bind.ivType.setImageResource(NotificationType.CONGRATS.imageResource)
            }
            NotificationType.NOTIFY.status -> {
                bind.ivType.setImageResource(NotificationType.NOTIFY.imageResource)
            }
        }
        if (item.isRead) {
            bind.constNotification.alpha = 0.8F
            bind.ivUnread.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
//        notifications.removeAt(position)
//        notifyItemRemoved(position)
//        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

}
