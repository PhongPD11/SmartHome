package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemBookVerticalBinding
import com.example.smartlamp.fragment.BooksFragment
import com.example.smartlamp.model.BookData


class BookAdapter(
    var context: Context,
    var books: List<BookData>,
    private val onBookClickInterface: BookClickInterface
) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(var binding: ItemBookVerticalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBookVerticalBinding.inflate(
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

        if (book.imageUrl != null) {
            Glide.with(context).load(book.imageUrl).error(R.drawable.ic_book_dummy)
                .into(holder.binding.ivBook)
        }

        binding.tvName.text = book.name
        if (book.rated != 0.0) {
            binding.tvRated.text = "${book.rated} (${book.userRate})"
        } else {
            binding.tvRated.visibility = View.GONE
        }
        var author = ""
        for (i in book.author.indices) {
            if (i < (book.author.size - 1)) {
                author += book.author[i].authorName + ", "
            } else {
                author += book.author[i].authorName
            }
        }
        binding.tvAuth.text = author

        holder.itemView.setOnClickListener {
            onBookClickInterface.onBookClick(book)
        }
    }


    override fun getItemCount(): Int {
        return books.size
    }

    fun updateList(list: List<BookData>) {
        this.books = list
        notifyDataSetChanged()
    }

    interface BookClickInterface {
        fun onBookClick(book: BookData)
    }

}