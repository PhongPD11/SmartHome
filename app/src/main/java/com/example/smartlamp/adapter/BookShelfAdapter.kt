package com.example.smartlamp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemNotificationBinding
import com.example.smartlamp.databinding.ItemUserBookBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.NotificationModel
import com.example.smartlamp.model.NotificationType
import com.example.smartlamp.model.UserBookData
import com.example.smartlamp.utils.ConvertTime

class BookShelfAdapter(
    var context: Context,
    var userBook: ArrayList<UserBookData>, val allBooks: ArrayList<BookData>
) : RecyclerView.Adapter<BookShelfAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ItemUserBookBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBookBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }



    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = userBook[position]
        val bind = holder.binding
        bind.tvTitle.setText(getNameByBookId(item.bookId, allBooks))

        when (item.status) {
            "registerBorrow" -> {
                bind.tvTime.setText(ConvertTime.formatDateExtra(item.createAt, "dd MMM yyyy - HH:mm"))
                bind.tvStatus.setText("Registering")
            }
            "borrowing" -> {
                bind.tvTime.setText(ConvertTime.formatDate(item.expireAt, "dd MMM yyyy - HH:mm"))
                bind.tvStatus.setText("Borrowing")
            }
            "delivering" -> {
                bind.tvTime.setText(ConvertTime.formatDate(item.expireAt, "dd MMM yyyy"))
                bind.tvStatus.setText("Delivering")
            }
            "borrowReturned" -> {
                bind.tvTime.apply {
                    setText(ConvertTime.formatDate(item.returnedAt, "dd MMM yyyy"))
                    setTitleText("Returned At:")
                }
                bind.tvStatus.setText("Returned")
            }
            "borrowExpired" -> {
                bind.tvTime.setText(ConvertTime.formatDate(item.expireAt, "dd MMM yyyy"))
                bind.tvStatus.apply {
                    setText("Borrow Expired")
                    setTextColor(resources.getColor(R.color.red))
                }
            }
            "borrowRegisterExpired" -> {
                bind.tvTime.setText(ConvertTime.formatDateExtra(item.createAt, "dd MMM yyyy"))
                bind.tvStatus.apply {
                    setText("Register Expired")
                    setTextColor(resources.getColor(R.color.red))
                }
            }

        }

    }

    private fun getNameByBookId(bookId: Long, books: ArrayList<BookData>): String? {
        val foundBook = books.find { it.bookId == bookId }
        return foundBook?.name
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return userBook.size
    }

}
