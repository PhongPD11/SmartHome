package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.adapter.BookShelfAdapter
import com.example.smartlamp.adapter.NotificationAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogNotificationBinding
import com.example.smartlamp.databinding.FragmentBookShelfBinding
import com.example.smartlamp.databinding.FragmentNotificationsBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.NotificationModel
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.model.UserBookData
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.ConvertTime
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class BookShelfFragment : Fragment() {
    lateinit var binding: FragmentBookShelfBinding

    private val viewModel: HomeViewModel by activityViewModels()
    var uid = 0

    var userBooks = ArrayList<UserBookData>()
    var filterList = ArrayList<UserBookData>()

    val books = ArrayList<BookData>()

    var selectedBook: BookData? = null

    @Inject
    lateinit var sharedPref: SharedPref
    @Inject
    lateinit var apiInterface: ApiInterface

    private lateinit var bookShelfAdapter: BookShelfAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookShelfBinding.inflate(layoutInflater)

        sharedPref = SharedPref(context)
        uid = sharedPref.getInt(UID)

        userBooks.clear()
        userBooks.addAll(viewModel.userBookList)

        binding.rvUserBook.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvUserBook,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        selectedBook = findBookById(books,filterList[position].bookId)
                        args.putSerializable(Constants.SELECTED_BOOK, selectedBook)
                        findNavController().navigate(R.id.navigation_book_detail, args)
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        setBook()
        setUI()
        return binding.root


    }

    private fun setBook(){
        viewModel.books.observe(viewLifecycleOwner) {
            Utils.setBook(it, null, books)
        }
    }

    private fun findBookById(bookList: ArrayList<BookData>, targetBookId: Long): BookData? {
        for (book in bookList) {
            if (book.bookId == targetBookId) {
                return book
            }
        }
        return null
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        filterList.clear()
        for (i in userBooks.indices) {
            if (!userBooks[i].status.isNullOrEmpty()) {
                filterList.add(userBooks[i])
            }
        }
        bookShelfAdapter = BookShelfAdapter(requireContext(), filterList, viewModel.allBooks)
        binding.rvUserBook.apply {
            adapter = bookShelfAdapter
            layoutManager = LinearLayoutManager(context)
        }

        bookShelfAdapter.notifyDataSetChanged()
    }

}