package com.example.smartlamp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemTagBinding

class AuthorBookAdapter(
    var context: Context,
    var authors: List<String>
) : RecyclerView.Adapter<AuthorBookAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AuthorBookAdapter.ViewHolder {
        return ViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AuthorBookAdapter.ViewHolder, position: Int) {
        holder.binding.tvTag.text = authors[position]
    }

    override fun getItemCount(): Int {
        return authors.size
    }

}