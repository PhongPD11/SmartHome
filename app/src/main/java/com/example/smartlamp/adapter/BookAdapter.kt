package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemBookBinding
import com.example.smartlamp.databinding.ItemBookHorizonBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.BookShowModel
import com.example.smartlamp.utils.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class BookAdapter(
    var context: Context,
    var books: List<BookData>
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemBookHorizonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookHorizonBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        val binding = holder.binding

        if (book.imageUrl != null){
            Glide.with(context).load(book.imageUrl).error(R.drawable.ic_book_dummy).into(holder.binding.ivBook)
        }

        binding.tvName.text = book.name
        if (book.rated != 0.0) {
            binding.tvRated.text = "${book.rated} (${book.userRate})"
        } else {
            binding.tvRated.visibility = View.GONE
        }
        var author = ""
        for (i in book.author.indices) {
            if (i < (book.author.size - 1)){
                author += book.author[i] + ", "
            } else {
                author += book.author[i]
            }
        }
        binding.tvAuth.text = author
    }


    override fun getItemCount(): Int {
        return books.size
    }

}