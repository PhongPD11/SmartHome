package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.databinding.ItemBookBinding
import com.example.smartlamp.model.BookShowModel
import com.example.smartlamp.utils.Utils

class FavoriteAdapter(
    var context: Context,
    var books: List<BookShowModel>,
    private val onBookClickInterface: BookClickInterface,
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = books[position]

        val rate = item.rate
        holder.binding.tvBook.text = item.name
        holder.binding.tvRate.text = rate.toString()
        Utils.showRating(
            rate,
            holder.binding.icStar1,
            holder.binding.icStar2,
            holder.binding.icStar3,
            holder.binding.icStar4,
            holder.binding.icStar5
        )
        holder.itemView.setOnClickListener {
            onBookClickInterface.onBookClick(item)
        }
    }


    override fun getItemCount(): Int {
        return books.size
    }

    interface BookClickInterface {
        fun onBookClick(room: BookShowModel)
    }

}