package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.smartlamp.R
import com.example.smartlamp.adapter.AuthorBookAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogConfirmRegisterBinding
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentBookDetailsBinding
import com.example.smartlamp.model.BookData
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.model.UserBook
import com.example.smartlamp.model.UserBookData
import com.example.smartlamp.utils.Constants.ADDRESS
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.BORROW
import com.example.smartlamp.utils.Constants.BORROWING
import com.example.smartlamp.utils.Constants.BORROW_EXPIRED
import com.example.smartlamp.utils.Constants.BORROW_RETURNED
import com.example.smartlamp.utils.Constants.BY_AUTHOR
import com.example.smartlamp.utils.Constants.FAVORITE
import com.example.smartlamp.utils.Constants.IS_DELIVERY
import com.example.smartlamp.utils.Constants.REGISTER_BORROW
import com.example.smartlamp.utils.Constants.RETURN
import com.example.smartlamp.utils.Constants.SEARCH_BY
import com.example.smartlamp.utils.Constants.SELECTED_BOOK
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener.Companion.setSingleClickListener
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

    private var address = ""
    private var isDelivery = false
    private var isFavorite = false
    private var status = ""


    private var bookId = 0L

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

        selectedBook = arguments?.get(SELECTED_BOOK) as BookData?

        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_book_dummy)
            .error(R.drawable.ic_book_dummy)

        setAuthors()
        setBook(selectedBook!!, options)

        val userRate = Utils.checkUserRated(bookId, viewModel.userBookList)
        setUserRate(userRate)
        setIconFavorite(Utils.checkUserFavorite(bookId, viewModel.userBookList))

        if (selectedBook!!.amount == 0) {
            binding.consStatus.visibility = View.VISIBLE
            binding.tvStatus.text = getString(R.string.out_of_book)
            binding.btnRegister.apply {
                isEnabled = false
                text = resources.getString(R.string.fullyBorrowed)
            }
        }
        statusCheck(bookId, viewModel.userBookList)

        binding.rvAuth.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvAuth,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        args.putString(SEARCH_BY, BY_AUTHOR)
                        viewModel.getBookByAuthor(authors[position])
                        findNavController().navigate(R.id.navigation_books, args)
                    }
                    override fun onLongItemClick(view: View?, position: Int) {}
                })
        )

        binding.icStar1.setSingleClickListener(singleClickListener)
        binding.icStar2.setSingleClickListener(singleClickListener)
        binding.icStar3.setSingleClickListener(singleClickListener)
        binding.icStar4.setSingleClickListener(singleClickListener)
        binding.icStar5.setSingleClickListener(singleClickListener)

        binding.btnBorrow.setSingleClickListener(singleClickListener)
        binding.btnReturn.setSingleClickListener(singleClickListener)

        binding.btnRegister.setSingleClickListener(singleClickListener)

        binding.ivBack.setSingleClickListener(singleClickListener)
        binding.ivFavorite.setSingleClickListener(singleClickListener)

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    private fun statusCheck(bookId: Long, useBookList : ArrayList<UserBookData>){
        val userBook = useBookList.find { it.bookId == bookId }
        if (userBook?.status != null) {
            when (userBook.status) {
                REGISTER_BORROW -> {
                    binding.cardRating.visibility = View.GONE
                    binding.cons1Btn.visibility = View.VISIBLE
                    binding.cons2Btn.visibility = View.GONE
                    binding.btnReturn.apply {
                        isEnabled = false
                        text = resources.getString(R.string.you_registered_this_book)
                    }
                    binding.consStatus.visibility = View.VISIBLE
                    binding.tvStatus.text = getString(R.string.you_registered_this_book)
                }
                BORROWING -> {
                    binding.cons1Btn.visibility = View.VISIBLE
                    binding.cons2Btn.visibility = View.GONE
                    binding.btnReturn.apply {
                        isEnabled = true
                        text = resources.getString(R.string.return_book)
                    }
                    binding.consStatus.visibility = View.VISIBLE
                    binding.tvStatus.text = getString(R.string.you_borrowed_this_book)
                }
                BORROW_RETURNED -> {

                }
                BORROW_EXPIRED -> {
                    binding.cons1Btn.visibility = View.VISIBLE
                    binding.cons2Btn.visibility = View.GONE
                    binding.btnReturn.apply {
                        text = context.getString(R.string.return_book)
                    }
                    binding.consStatus.visibility = View.VISIBLE
                    binding.tvStatus.text = resources.getString(R.string.borrow_expired)
                }
                else -> {
                    binding.cardRating.visibility = View.GONE
                }
            }
        }
    }

    private fun setUserRate(userRate: Int) {
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
        binding.tvBook.setText(book.name)
        binding.tvBookId.setText(book.bookId.toString())
        bookId = book.bookId
        binding.tvBookMajor.setText(book.major)
        binding.tvBookType.setText(book.type)
        binding.tvBookDDC.setText(book.ddc)
        binding.tvBookLanguage.setText(book.language)
        binding.tvBookLocation.setText(book.bookLocation)
        if (book.rated != 0.0) {
            binding.tvRated.text = "${book.rated} (${book.userRate})"
        } else {
            binding.tvRated.visibility = View.GONE
        }
        if (book.amount <= 0) {
            binding.btnRegister.apply {
                isEnabled = false
                text = resources.getString(R.string.fullyBorrowed)
            }
        }

        authors.clear()
        var author = ""
        for (i in book.author.indices) {
            authors.add(book.author[i].authorName)
            author += if (i < (book.author.size - 1)) {
                book.author[i].authorName + ", "
            } else {
                book.author[i].authorName
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        authorAdapter.notifyDataSetChanged()
    }

    private fun showDialogConfirm(context: Context) {
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogConfirmRegisterBinding =
            DialogConfirmRegisterBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.35).toInt()
        dialog.window?.setLayout(width, height)

        bindingDialog.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                bindingDialog.consAddress.visibility = View.VISIBLE
                isDelivery = true
            } else {
                bindingDialog.consAddress.visibility = View.GONE
                isDelivery = false
            }
        }

        bindingDialog.btnYes.setOnClickListener {
            if (isDelivery) {
                address = bindingDialog.etAddress.text.toString()
                if (address.isNullOrEmpty()) {
                    bindingDialog.tvError.visibility = View.VISIBLE
                } else {
                    bindingDialog.tvError.visibility = View.GONE
                    dialog.dismiss()
                    registerBook(
                        hashMapOf(
                            UID to sharedPref.getInt(UID),
                            BOOK_ID to bookId,
                            ADDRESS to address,
                            IS_DELIVERY to isDelivery
                        )
                    )
                }
            } else {
                dialog.dismiss()
                registerBook(
                    hashMapOf(
                        UID to sharedPref.getInt(UID),
                        BOOK_ID to bookId,
                        ADDRESS to address,
                        IS_DELIVERY to isDelivery
                    )
                )
            }
        }
        dialog.show()
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
                    viewModel.getTopBook()
                    Utils.showSimpleDialog(context!!, "Rating", "Thank you for rating!", findNavController())
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
                viewModel.getUserBook(uid)
                setIconFavorite(isFavorite)
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {}
        })
    }

    private fun registerBook(map: HashMap<String?, Any?>) {
        apiInterface.registerBook(map).enqueue(object : Callback<UserBook> {
            override fun onResponse(call: Call<UserBook>, response: Response<UserBook>) {
                if (response.body()?.data != null && response.body()!!.code == 200) {
                    Utils.showSimpleDialog(context!!, "", getString(R.string.register_success), findNavController())
                    viewModel.getUserBook(sharedPref.getInt(UID))
                    binding.btnRegister.apply {
                        isEnabled = false
                        text = resources.getString(R.string.registered)
                    }
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserBook>, t: Throwable) {
                t.printStackTrace()
            }
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
            R.id.btnRegister -> {
                showDialogConfirm(requireContext())
            }
            R.id.btnReturn -> {
                val args = Bundle()
                args.putString("from", RETURN)
                args.putLong(BOOK_ID, selectedBook!!.bookId)
                findNavController().navigate(R.id.navigation_qr_scan, args)
            }
            R.id.btnBorrow -> {
                val args = Bundle()
                args.putString("from", BORROW)
                args.putLong(BOOK_ID, selectedBook!!.bookId)
                findNavController().navigate(R.id.navigation_qr_scan, args)
            }
        }
    }

}