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
import com.example.smartlamp.databinding.FragmentAllBookBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils.Companion.isFavoriteBook
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllBookFragment : Fragment() {
    lateinit var binding: FragmentAllBookBinding

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    val books = ArrayList<BookData>()

    var selectedBook: BookData? = null

    private lateinit var bookAdapter: BookAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllBookBinding.inflate(layoutInflater)

        sharedPref = SharedPref(context)

        binding.rvBook.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvBook,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        selectedBook = books[position]
                        args.putSerializable(Constants.IS_FAVORITE, isFavoriteBook(selectedBook!!, viewModel.favoriteList))
                        args.putSerializable(Constants.FAVORITE, selectedBook)
                        findNavController().navigate(R.id.navigation_book_detail, args)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        setObserb();
        setUI()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserb() {
        viewModel.books.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val listBook = it.data
                books.clear()
                books.addAll(listBook)
                setUI()
                if (books.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
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

}