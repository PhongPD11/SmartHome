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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.ModeAdapter
import com.example.smartlamp.adapter.RoomAdapter
import com.example.smartlamp.adapter.RoomDetailAdapter
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.databinding.FragmentLightModeBinding
import com.example.smartlamp.model.DailyForecast
import com.example.smartlamp.model.DeviceModel
import com.example.smartlamp.model.ModeModel
import com.example.smartlamp.model.RoomModel
import com.example.smartlamp.model.ScheduleModel
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.viewmodel.HomeViewModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.slider.Slider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class LightModeFragment: Fragment() , ModeAdapter.ModeClickInterface{
    private lateinit var binding: FragmentLightModeBinding

    private var state = -1
    private var brightness = -1F
    private var pos = -1

    private val viewModel: LampViewModel by activityViewModels()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    private val workMode = ModeModel( 100F, R.drawable.working, "Working", 0, false)
    private val readingMode = ModeModel( 80F, R.drawable.reading_book, "Reading", 0, false)
    private val sleepingMode = ModeModel( 30F,R.drawable.sleep, "Sleeping", 0, false)
    private val childrenMode = ModeModel(60F, R.drawable.playtime, "Playing Time", 1, false)
    private val modes = listOf(workMode, readingMode, sleepingMode, childrenMode)

    private lateinit var modeAdapter: ModeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentLightModeBinding.inflate(layoutInflater)

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                val value = slider.value
                ledNodeRef.child("brightness").setValue(value)
            }
        })


        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.swDevice.setOnCheckedChangeListener { _, isChecked ->
            state = if(isChecked)
                1
            else
                0
            ledNodeRef.child("state").setValue(state)
        }

        viewModel.getLampData().observe(viewLifecycleOwner){
            if(it!= null) {
                state = it.state
                brightness = it.brightness
                setUI()
            }
        }

        setUI()
        return binding.root
    }

    private fun controlLamp(state: Int){
        if (state == 1){
            binding.swDevice.isChecked = true
            binding.ivLamp.setImageResource(R.drawable.lamp_on)
        } else {
            binding.ivLamp.setImageResource(R.drawable.lamp)
            binding.swDevice.isChecked = false
        }
    }
    @SuppressLint("NotifyDataSetChanged")private fun onlyOneMode(position: Int) {
        for (i in modes.indices) {
            modes[i].modeOn = false
        }
        if (position != -1) {
            modes[position].modeOn = true
            pos = position
        }
       // modeAdapter.notifyDataSetChanged()
        setUI()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        controlLamp(state)
        binding.slider.value = brightness

        modeAdapter = ModeAdapter(requireContext(), modes, this)
        binding.rvMode.apply {
            adapter = modeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            binding.rvMode.scrollToPosition(pos)
        }
        modeAdapter.notifyDataSetChanged()
    }


    override fun onModeClick(position: Int) {
        onlyOneMode(position)
        brightness = modes[position].brightness
        binding.slider.value = brightness
        ledNodeRef.child("brightness").setValue(brightness)

    }

}
