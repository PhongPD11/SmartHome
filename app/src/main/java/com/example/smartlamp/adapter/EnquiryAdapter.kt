package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemEnquiryBinding
import com.example.smartlamp.databinding.ItemNotificationBinding
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.utils.ConvertTime


class EnquiryAdapter(
    var context: Context,
    var enquiryList: ArrayList<EnquiryResponse.Data>,
) : RecyclerView.Adapter<EnquiryAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemEnquiryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEnquiryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = enquiryList[position]
        val bind = holder.binding
        bind.tvTitle.text = item.title
        bind.tvContent.text = "Description: ${item.content}"
        bind.tvTime.text = ConvertTime.formatDate(item.createAt, "dd MMM yyyy - HH:mm")

        when (item.status.uppercase()) {
            "Đã phản hồi" -> {
                bind.ivUnread.visibility = View.VISIBLE
            }
            "Đóng" -> {
                bind.ivUnread.visibility = View.GONE
                bind.constNotification.alpha = 0.3f
            }
            else -> {
                bind.ivUnread.visibility = View.GONE
            }
        }
    }


    override fun getItemCount(): Int {
        return enquiryList.size
    }

}
