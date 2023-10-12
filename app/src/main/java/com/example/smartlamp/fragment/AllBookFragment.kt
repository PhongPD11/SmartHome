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
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class AllBookFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding: FragmentAllBookBinding

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    val books = ArrayList<BookData>()

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

        isShowMenu = false
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

        binding.ivFilter.setOnClickListener(singleClick)
        binding.ivSearch.setOnClickListener(singleClick)
        binding.ivMenu.setOnClickListener(singleClick)

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

    override fun onItemClick(view: View) {
        when(view.id) {
            R.id.iv_menu -> {
                isShowMenu = !isShowMenu
                if (isShowMenu) {
                    binding.consMenu.visibility = View.VISIBLE
                    binding.rvBook.visibility = View.GONE
                } else {
                    binding.consMenu.visibility = View.GONE
                    binding.rvBook.visibility = View.VISIBLE
                }
            }
            R.id.iv_search -> {

            }
            R.id.iv_filter -> {

            }
        }
    }

}