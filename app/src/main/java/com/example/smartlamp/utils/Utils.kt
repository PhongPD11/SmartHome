package com.example.smartlamp.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.BookModel
import com.example.smartlamp.model.UserBookData
import com.example.smartlamp.utils.Constants.LOGIN
import com.google.android.material.textview.MaterialTextView

class Utils {
    companion object {
        private val dotw = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        fun repeatDisplay(repeat: String): String {
            var timeDisplay = ""
            if (repeat == "Daily" || repeat == "Once") {
                timeDisplay = repeat
            } else {
                for (charIndex in repeat.indices) {
                    if (repeat[charIndex].toString() == "1") {
                        timeDisplay += "${dotw[charIndex]} "
                    }
                }
            }
            return timeDisplay
        }

        fun updateTime(hour: Int, min: Int): String {
            var display = ""
            var displayHour = ""
            var displayMin = ""
            displayHour = if (hour < 10) {
                "0${hour}"
            } else {
                hour.toString()
            }
            displayMin = if (min < 10) {
                "0${min}"
            } else {
                min.toString()
            }
            display = "$displayHour:$displayMin"
            return display
        }

        fun showRating(rate: Double, vararg stars: ImageView) {
            val starDrawables = listOf(
                R.drawable.ic_star,
                R.drawable.ic_star_half,
                R.drawable.ic_star_empty
            )
            val filledStars = rate.toInt()
            val haveHalfStar = (rate - filledStars >= 0.5)

            for (i in stars.indices) {
                if (i + 1 <= filledStars) {
                    stars[i].setImageResource(starDrawables[0])
                    println("Star: $i Full star")
                } else {
                    if (haveHalfStar) {
                        stars[i].setImageResource(starDrawables[1])
                        println("Star: $i Half star")
                    }
                    break
                }
            }
        }

        fun userRating(rate: Int, vararg stars: ImageView) {
            for (i in stars.indices) {
                if (i + 1 <= rate) {
                    stars[i].setImageResource(R.drawable.ic_star)
                } else {
                    stars[i].setImageResource(R.drawable.ic_star_empty)
                }
            }
        }

        fun checkUserRated(bookId: Long, useBookList: ArrayList<UserBookData>): Int {
            val userBook = useBookList.find { it.bookId == bookId }
            return if (userBook != null) {
                userBook.rate ?: 0
            } else 0
        }

        fun checkUserFavorite(bookId: Long, useBookList: ArrayList<UserBookData>): Boolean {
            val userBook = useBookList.find { it.bookId == bookId }
            return if (userBook != null) {
                userBook.isFavorite ?: false
            } else false
        }

        fun isFavoriteBook(book: BookData, listFav: ArrayList<BookData>): Boolean {
            return listFav.contains(book)
        }


        fun showSimpleDialog(context: Context, title: String, subTitle: String, nav: NavController) {
            val dialog = Dialog(context, R.style.CustomDialogTheme)
            val bindingDialog: DialogSuccessBinding =
                DialogSuccessBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(bindingDialog.root)
//        binding.progressBar.visibility = View.GONE
            val window = dialog.window
            val params = window?.attributes

            params?.gravity = Gravity.CENTER
            window?.attributes = params

            if (title.isNotEmpty()) {
                bindingDialog.tvTitle.apply {
                    text = title
                    visibility = View.VISIBLE
                }
            }

            if (subTitle.isNotEmpty()) {
                bindingDialog.tvSubTitle.apply {
                    text = subTitle
                    visibility = View.VISIBLE
                }
            }

            bindingDialog.btnYes.setOnClickListener {
                dialog.dismiss()

            }
            dialog.setOnDismissListener {
                nav.popBackStack()
            }
            dialog.show()
        }

        fun setBook(
            booksResponse: BookModel,
            tvEmpty: MaterialTextView?,
            books: ArrayList<BookData>
        ) {
            if (booksResponse.code == 200) {
                val listBook = booksResponse.data
                books.clear()
                books.addAll(listBook)
                if (books.isEmpty()) {
                    tvEmpty?.visibility = View.VISIBLE
                } else {
                    tvEmpty?.visibility = View.GONE
                }
            }
        }

        fun setRecommendationBook(
            major: String,
            booksResponse: BookModel,
            books: ArrayList<BookData>
        ){
            if (booksResponse.code == 200) {
                val listBook = booksResponse.data
                books.clear()
                for (i in listBook.indices) {
                    when (major) {

                    }
                }
            }
        }

        fun loginToNavigate(sharedPref: SharedPref, nav: NavController, resId: Int, args: Bundle?) {
            if (!sharedPref.getBoolean(LOGIN)) {
                nav.navigate(R.id.navigation_auth)
            } else {
                nav.navigate(resId, args)
            }
        }

    }
}