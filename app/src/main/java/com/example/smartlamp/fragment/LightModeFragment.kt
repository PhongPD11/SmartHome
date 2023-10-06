package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.ModeAdapter
import com.example.smartlamp.databinding.FragmentLightModeBinding
import com.example.smartlamp.model.ModeModel
import com.google.android.material.slider.Slider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LightModeFragment: Fragment() , ModeAdapter.ModeClickInterface{
    private lateinit var binding: FragmentLightModeBinding

    private var state = -1
    private var flicker = -1
    private var brightness = -1F
    private var pos = -1

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    private val modes = mutableListOf<ModeModel>()

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

        binding.swFlicker.setOnCheckedChangeListener { _, isChecked ->
            flicker = if(isChecked)
                1
            else
                0
            ledNodeRef.child("flicker").setValue(flicker)
        }

        setUI()
        return binding.root
    }

    private fun controlLamp(state: Int){
//        if (state == 1){
//            binding.swDevice.isChecked = true
//            binding.ivLamp.setImageResource(R.drawable.lamp_on)
//        } else {
//            binding.ivLamp.setImageResource(R.drawable.lamp)
//            binding.swDevice.isChecked = false
//        }
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
        binding.swFlicker.isChecked = flicker == 1

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
