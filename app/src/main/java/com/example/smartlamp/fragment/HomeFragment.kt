package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.RoomAdapter
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.model.DailyForecast
import com.example.smartlamp.model.RoomModel
import com.example.smartlamp.utils.Constants.NO_CLOUD
import com.example.smartlamp.utils.Constants.SMALL_SUN
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.viewmodel.HomeViewModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    var data = MutableLiveData<List<DailyForecast>>()
    private val viewModel: HomeViewModel by activityViewModels()
    private val lampViewModel: LampViewModel by activityViewModels()

    private var welcome = ""
    private var userName = ""
    private val auth = FirebaseAuth.getInstance()


    private val livingRoom = RoomModel(R.drawable.living_room, "Living Room")
    private val bedroom = RoomModel(R.drawable.bed_room, "Bedroom")
    private val kitchenRoom = RoomModel(R.drawable.kitchen, "Kitchen")
    private val rooms = listOf(livingRoom, bedroom, kitchenRoom)


    private lateinit var roomAdapter: RoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        lampViewModel.startObservingKey()
        binding.tvWelcome.text = ""

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            lampViewModel.uid.value = currentUser.uid
            lampViewModel.startObservingUser(currentUser.uid)
            lampViewModel.getUserData().observe(viewLifecycleOwner) {
                if (it != null) {
                    userName = ", ${it.firstName}"
                }
            }
        }

        binding.rvRoom.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvRoom,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val name = rooms[position].room
                        val bundle = bundleOf("name" to name)
                        findNavController().navigate(R.id.navigation_room, bundle)
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )
        lampViewModel.getLampData()

        setObserb()
        setUI()
        isNight()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setObserb() {
        binding.tvStatus.visibility = View.GONE
        binding.tvTemp.visibility = View.GONE
        binding.tvPosition.visibility = View.GONE
        binding.ivWeather.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            val dailyForecast = weather.dailyForecasts[0]
            val temperature = dailyForecast.temperature

            binding.tvStatus.visibility = View.VISIBLE
            binding.tvTemp.visibility = View.VISIBLE
            binding.tvPosition.visibility = View.VISIBLE
            binding.ivWeather.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

            if (isNight()) {
                val status = dailyForecast.night.icon
                binding.tvStatus.text = status
                if (dailyForecast.night.hasPrecipitation) {
                    binding.ivWeather.setImageResource(R.drawable.night_rain)
                } else {
                    when (status) {
                        NO_CLOUD -> binding.ivWeather.setImageResource(R.drawable.night)
                        else -> binding.ivWeather.setImageResource(R.drawable.night_cloud)
                    }
                }
            } else {
                val status = dailyForecast.day.icon
                binding.tvStatus.text = dailyForecast.day.icon
                if (dailyForecast.night.hasPrecipitation) {
                    binding.ivWeather.setImageResource(R.drawable.day_rain)
                } else {
                    when (status) {
                        SMALL_SUN -> binding.ivWeather.setImageResource(R.drawable.partly_sunny)
                        else -> binding.ivWeather.setImageResource(R.drawable.blazing_sunshine)
                    }
                }
            }
            binding.tvTemp.text = ConvertTemp(temperature.maximum.unit, temperature.maximum.value)
        }

    }

    private fun ConvertTemp(type: String, value: Double): String {
        var temp = value
        if (type == "F") {
            temp = round((value - 32) * 5 / 9 * 100) / 100
        }
        return "$temp °C"
    }

    private fun isNight(): Boolean {
        var isNight = true
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        if (hour in 5..18) {
            welcome = "Good Morning$userName"
            isNight = false
            if (hour in 12..17) {
                welcome = "Good Afternoon$userName"
            }
        } else if (hour in 18..21) {
            welcome = "Good Evening$userName"
        } else {
            welcome = "Good Night$userName"
        }

    binding.tvWelcome.text = welcome
    return isNight
}

@SuppressLint("NotifyDataSetChanged")
private fun setUI() {
    roomAdapter = RoomAdapter(requireContext(), rooms)
    binding.rvRoom.apply {
        adapter = roomAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    roomAdapter.notifyDataSetChanged()
}

}