package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.smartlamp.R
import com.example.smartlamp.adapter.AuthorBookAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentBookDetailsBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants.FAVORITE
import com.example.smartlamp.utils.Constants.IS_FAVORITE
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class BookDetailsFragment : Fragment(), OnItemSingleClickListener {
    private lateinit var binding: FragmentBookDetailsBinding

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    private lateinit var authorAdapter: AuthorBookAdapter

    private var isFavorite = false

    private var bookId = 0

    private val authors = ArrayList<String>()

    var selectedBook: BookData? = null

    private val viewModel: HomeViewModel by activityViewModels()

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentBookDetailsBinding.inflate(layoutInflater)
        val singleClickListener = SingleClickListener(this)
        sharedPref = SharedPref(context)

        isFavorite = arguments?.getBoolean(IS_FAVORITE) == true
        selectedBook = arguments?.get(FAVORITE) as BookData?
        setIconFavorite(isFavorite)

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_book_dummy)
            .error(R.drawable.ic_book_dummy)

        setAuthors()
        setBook(selectedBook!!, options)

        val userRate = Utils.checkUserRated(bookId, viewModel.userBookList)
        setUserRate(userRate)

        binding.icStar1.setOnClickListener(singleClickListener)
        binding.icStar2.setOnClickListener(singleClickListener)
        binding.icStar3.setOnClickListener(singleClickListener)
        binding.icStar4.setOnClickListener(singleClickListener)
        binding.icStar5.setOnClickListener(singleClickListener)

        binding.ivBack.setOnClickListener(singleClickListener)
        binding.ivFavorite.setOnClickListener(singleClickListener)

        return binding.root
    }

    private fun setUserRate(userRate: Int){
        if (userRate > 0) {
            Utils.userRating(
                userRate,
                binding.icStar1,
                binding.icStar2,
                binding.icStar3,
                binding.icStar4,
                binding.icStar5
            )
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setBook(book: BookData, options: RequestOptions) {
        binding.tvName.text = book.name
        binding.tvBook.text = book.name
        binding.tvBookId.text = book.bookId.toString()
        bookId = book.bookId
        binding.tvBookMajor.text = book.major
        binding.tvBookType.text = book.type
        if (book.rated != 0.0) {
            binding.tvRated.text = "${book.rated} (${book.userRate})"
        } else {
            binding.tvRated.visibility = View.GONE
        }
        authors.clear()
        var author = ""
        for (i in book.author.indices) {
            authors.add(book.author[i])
            if (i < (book.author.size - 1)) {
                author += book.author[i] + ", "
            } else {
                author += book.author[i]
            }
        }
        binding.tvAuth.text = author
        authorAdapter.notifyDataSetChanged()
        Glide.with(this).load(book.imageUrl).apply(options).into(binding.ivBook)
    }

    private fun setIconFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_heart_empty)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAuthors() {
        authorAdapter = AuthorBookAdapter(requireContext(), authors)
        binding.rvAuth.apply {
            adapter = authorAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 4)
        }
        authorAdapter.notifyDataSetChanged()
    }

    private fun rating(star: Int) {
        val uid = sharedPref.getInt(UID)
        apiInterface.userRateBook(uid, bookId, star).enqueue(object : Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                if (response.body()?.data != null) {
                    setUserRate(star)
                    viewModel.getUserBook(uid)
                    viewModel.getBooks()
                    viewModel.getFavorites(uid)
                }
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun makeFavorite() {
        isFavorite = !isFavorite
        val uid = sharedPref.getInt(UID)
        apiInterface.makeFavorite(uid, bookId, isFavorite).enqueue(object :
            Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                if (response.body()?.data == null) {
                    isFavorite = !isFavorite
                }
                viewModel.getFavorites(uid)
                setIconFavorite(isFavorite)
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {}
        })
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.ivFavorite -> {
                makeFavorite()
            }
            R.id.ic_star1 -> {
                rating(1)
            }
            R.id.ic_star2 -> {
                rating(2)
            }
            R.id.ic_star3 -> {
                rating(3)
            }
            R.id.ic_star4 -> {
                rating(4)
            }
            R.id.ic_star5 -> {
                rating(5)
            }
        }
    }

}