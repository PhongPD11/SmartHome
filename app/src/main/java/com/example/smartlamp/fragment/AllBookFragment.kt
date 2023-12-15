package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.BookAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentAllBookBinding
import com.example.smartlamp.model.AuthorData
import com.example.smartlamp.model.BookData
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import javax.inject.Inject


@AndroidEntryPoint
class AllBookFragment : Fragment(), OnItemSingleClickListener, BookAdapter.BookClickInterface {
    lateinit var binding: FragmentAllBookBinding

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    val books = ArrayList<BookData>()
    var filterBooks = ArrayList<BookData>()

    var isShowMenu = false

    var selectedBook: BookData? = null

    private lateinit var bookAdapter: BookAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBookBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().isNotEmpty()) {
                filter(text.toString().trim())
            } else {
                books.let { bookAdapter.updateList(it) }
            }
        }

        setObserve()
        setUI()

        binding.ivFilter.setOnClickListener(singleClick)
        binding.ivSearch.setOnClickListener(singleClick)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserve() {
        viewModel.books.observe(viewLifecycleOwner) {
            Utils.setBook(it, binding.tvEmpty, books)
            setUI()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        bookAdapter = BookAdapter(requireContext(), books, this)
        binding.rvBook.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        bookAdapter.notifyDataSetChanged()
    }

    fun filter(text: String?) {
        val temp: ArrayList<BookData> = ArrayList()
        for (country in books) {
            if (text?.let { country.name.contains(it, true) } == true ||
                text?.let { country.bookId.toString().contains(it, true) } == true ||
                text?.let { country.ddc.contains(it, true) } == true ||
                text?.let { country.major.contains(it, true) } == true || authorFilter(text, country.author)

            ) {
                temp.add(country)
            }
        }
        filterBooks = temp
        bookAdapter.updateList(filterBooks)
    }

    fun authorFilter (text: String?, authors: List<AuthorData>) : Boolean {
        var existBook = false
        for (author in authors){
            if (text?.let { author.authorName.contains (it, true) } == true) {
                existBook = true
                break
            }
        }
        return existBook
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_search -> {

            }
            R.id.iv_filter -> {

            }
        }
    }

    override fun onBookClick(book: BookData) {
        val args = Bundle()
        args.putSerializable(Constants.SELECTED_BOOK, book)
        findNavController().navigate(R.id.navigation_book_detail, args)
        binding.edtSearch.setText("")
    }

}