package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemNotificationBinding

class NotificationAdapter(
    var context: Context,
//    var notifications: ArrayList<MainNotificationModel.Notifications.notificationData>,
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
//        val item = notifications[position]
//        val bind = holder.binding
//        bind.tvTitle.text = item.title
//        bind.tvContent.text = item.content
//        bind.tvTime.text = ConvertTime.formatDate(item.time, "dd MMM yyyy - HH:mm:ss")
//
//        var imgType = bind.ivTypeMsg
//
//        if (item.read == "UNREAD") {
//            bind.ivUnread.visibility = View.VISIBLE
//        } else {
//            bind.ivUnread.visibility = View.GONE
//        }
//
//        if (item.typeMsg == "Present") {
//            imgType.setImageResource(R.drawable.ic_gift)
//        } else {
//            imgType.setImageResource(R.drawable.ic_notification)
//        }


    }


    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(position: Int) {
//        notifications.removeAt(position)
//        notifyItemRemoved(position)
//        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return 1
    }

}
