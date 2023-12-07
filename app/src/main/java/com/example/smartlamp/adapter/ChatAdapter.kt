package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemChatBinding
import com.example.smartlamp.databinding.ItemEnquiryBinding
import com.example.smartlamp.databinding.ItemNotificationBinding
import com.example.smartlamp.model.EnquiryDetailResponse
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.utils.ConvertTime


class ChatAdapter(
    var context: Context,
    private var enquiry: ArrayList<EnquiryDetailResponse.Data>,
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = enquiry[position]
        val bind = holder.binding

        val time = ConvertTime.formatDate(item.time, "dd MMM yyyy - HH:mm")

        if (item.isAdmin){
            bind.cardAdmin.visibility = View.VISIBLE
            bind.cardUser.visibility = View.GONE
            bind.tvAdminMessage.text = item.content
            bind.tvAdminTime.text = time
        } else {
            bind.cardUser.visibility = View.VISIBLE
            bind.cardAdmin.visibility = View.GONE
            bind.tvUserMessage.text = item.content
            bind.tvUserTime.text = time
        }
    }


    override fun getItemCount(): Int {
        return enquiry.size
    }

}
