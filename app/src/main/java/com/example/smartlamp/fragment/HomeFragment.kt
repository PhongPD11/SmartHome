package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.FavoriteAdapter
import com.example.smartlamp.databinding.DialogYesNoBinding
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.model.BookShowModel
import com.example.smartlamp.model.DailyForecast
import com.example.smartlamp.model.Quote
import com.example.smartlamp.utils.Constants.LOGIN
import com.example.smartlamp.utils.Constants.NAME
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class HomeFragment : Fragment(), FavoriteAdapter.BookClickInterface {
    private lateinit var binding: FragmentHomeBinding

    var data = MutableLiveData<List<DailyForecast>>()
    private val viewModel: HomeViewModel by activityViewModels()

    private var welcome = ""
    private var signed = false

    private val quote = Quote("“In principle and reality, libraries are life-enhancing palaces of wonder.”", "―Gail Honeyman")
    private val quote2 = Quote("“I am an omnivorous reader with a strangely retentive memory for trifles.”", "―Arthur Conan Doyle")
    private val quote3 = Quote("“In the end, we’ll all become stories.”", "―Margaret Atwood")
    private val quote4 = Quote("“If you only read the books that everyone else is reading, you can only think what everyone else is thinking.”", "―Haruki Murakami")
    private val listQuote = listOf<Quote>(quote, quote2, quote3, quote4)

    @Inject
    lateinit var sharedPref: SharedPref

    private var favorites = ArrayList<BookShowModel>()


    private lateinit var favoriteAdapter: FavoriteAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sharedPref = SharedPref(context)

        signed = sharedPref.getBoolean(LOGIN)

        if (signed) {
            binding.cvFavorite.visibility = View.VISIBLE
        }

        val name = sharedPref.getString(NAME)
        if (!name.isNullOrEmpty()) {
            binding.tvName.apply {
                visibility = View.VISIBLE
                text = name
            }
        } else {
            binding.tvName.visibility = View.GONE
        }

        setQuote();

        setObserb()
        setUI()
        isNight()

        return binding.root
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun setObserb() {
        viewModel.favorites.observe(viewLifecycleOwner) {
            if (it.code == 200) {
                val listBook = it.data
                favorites.clear()
                for (i in listBook.indices) {
                    val book = BookShowModel("", listBook[i].name, listBook[i].vote)
                    if (listBook[i].imageUrl != null) {
                        book.image = listBook[i].imageUrl.toString()
                    }
                    if (favorites.isNotEmpty()){
                        binding.tvEmpty.visibility = View.GONE
                    } else {
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    favorites.add(book)
                    favoriteAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setQuote(){
        val random = (listQuote.indices).random()
        binding.tvQuote.text = listQuote[random].quote
        binding.tvAuth.text = listQuote[random].author
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(context: Context) {
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogYesNoBinding = DialogYesNoBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)

        val window = dialog.window
        val params = window?.attributes

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        bindingDialog.tvTitle.text = getString(R.string.guest_user)

        bindingDialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.btnYes.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_auth)
        }
        dialog.show()
    }

    private fun isNight(): Boolean {
        var isNight = true
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        if (hour in 5..18) {
            welcome = "Good Morning"
            isNight = false
            if (hour in 12..17) {
                welcome = "Good Afternoon"
            }
        } else if (hour in 18..21) {
            welcome = "Good Evening"
        } else {
            welcome = "Good Night"
        }

        binding.tvWelcome.text = welcome
        return isNight
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        favoriteAdapter = FavoriteAdapter(requireContext(), favorites, this)
        binding.rvFav.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        favoriteAdapter.notifyDataSetChanged()
    }

//    override fun onRoomClick(room: BookShowModel) {
//        val name = room.room
//        val bundle = bundleOf("name" to name)
//        if (signed) {
//            findNavController().navigate(R.id.navigation_room, bundle)
//        } else {
//            showDialog(requireContext())
//        }
//    }

    override fun onBookClick(room: BookShowModel) {
        findNavController().navigate(R.id.navigation_room)
    }

}