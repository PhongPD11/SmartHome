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
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.IS_FAVORITE
import com.example.smartlamp.utils.Constants.POSITION
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
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

        val position = arguments?.get(POSITION)
        isFavorite = arguments?.getBoolean(IS_FAVORITE) == true

        setIconFavorite(isFavorite)

        binding.tvName.text = sharedPref.getString(Constants.FULL_NAME)

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_book_dummy)
            .error(R.drawable.ic_book_dummy)

        binding.ivBack.setOnClickListener(singleClickListener)
        binding.ivFavorite.setOnClickListener(singleClickListener)

        setAuthors();

        viewModel.favorites.observe(viewLifecycleOwner) {
            if (!it.data.isNullOrEmpty() && position != null) {
                val book = it.data[position as Int]
                binding.tvName.text = book.name
                binding.tvBook.text = book.name
                binding.tvBookId.text = book.bookId.toString()
                bookId = book.bookId
                binding.tvBookMajor.text = book.major
                binding.tvBookType.text = book.type
                binding.tvRated.text = book.vote.toString()
                authors.clear()
                var author = ""
                for (i in book.author.indices) {
                    authors.add(book.author[i])
                    if (i < (book.author.size - 1)){
                        author += book.author[i] + ", "
                    } else {
                        author += book.author[i]
                    }
                }
                binding.tvAuth.text = author
                authorAdapter.notifyDataSetChanged()
                Glide.with(this).load(book.imageUrl).apply(options).into(binding.ivBook)
            }
        }

        return binding.root
    }

    private fun setIconFavorite(isFavorite : Boolean){
        if (isFavorite) {
            binding.ivFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            binding.ivFavorite.setImageResource(R.drawable.ic_heart_empty)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAuthors(){
        authorAdapter = AuthorBookAdapter(requireContext(), authors)
        binding.rvAuth.apply {
            adapter = authorAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 4)
        }
        authorAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View) {
        when(view.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.ivFavorite -> {
                isFavorite = !isFavorite
                val uid = sharedPref.getInt(UID)
                apiInterface.makeFavorite(uid, bookId, isFavorite).enqueue(object :
                    Callback<SimpleApiResponse> {
                    override fun onResponse(
                        call: Call<SimpleApiResponse>,
                        response: Response<SimpleApiResponse>
                    ) {
                        if (response.body()?.data == null){
                            isFavorite = !isFavorite
                        }
                        setIconFavorite(isFavorite)
                    }

                    override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {}
                })
            }
        }
    }

}