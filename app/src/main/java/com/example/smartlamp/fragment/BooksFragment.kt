package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.BookAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentBooksBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.ALL
import com.example.smartlamp.utils.Constants.BY_AUTHOR
import com.example.smartlamp.utils.Constants.BY_MAJOR
import com.example.smartlamp.utils.Constants.BY_TYPE
import com.example.smartlamp.utils.Constants.SEARCH_BY
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class BooksFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding: FragmentBooksBinding

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    var searchBy = ALL

    val books = ArrayList<BookData>()

    var selectedBook: BookData? = null

    private lateinit var bookAdapter: BookAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBooksBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)

        searchBy = arguments?.getString(SEARCH_BY).toString()

        binding.rvBook.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvBook,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        selectedBook = books[position]
                        args.putSerializable(Constants.SELECTED_BOOK, selectedBook)
                        findNavController().navigate(R.id.navigation_book_detail, args)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        setObserve()

        binding.ivFilter.setOnClickListener(singleClick)
        binding.ivSearch.setOnClickListener(singleClick)
        binding.ivBack.setOnClickListener(singleClick)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserve() {
        when (searchBy) {
            ALL -> {
                viewModel.books.observe(viewLifecycleOwner) {
                    Utils.setBook(it, binding.tvEmpty, books)
                }
            }
            BY_AUTHOR -> {
                viewModel.authorBook.observe(viewLifecycleOwner) {
                    Utils.setBook(it, binding.tvEmpty, books)
                    setUI()
                }
            }
            BY_MAJOR -> {
                viewModel.majorBooks.observe(viewLifecycleOwner) {
                    Utils.setBook(it, binding.tvEmpty, books)
                    setUI()
                }
            }
            BY_TYPE -> {
                viewModel.majorBooks.observe(viewLifecycleOwner) {
                    Utils.setBook(it, binding.tvEmpty, books)
                    setUI()
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        bookAdapter = BookAdapter(requireContext(), books)
        binding.rvBook.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        bookAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                findNavController().popBackStack()
            }
            R.id.iv_search -> {

            }
            R.id.iv_filter -> {

            }
        }
    }

}