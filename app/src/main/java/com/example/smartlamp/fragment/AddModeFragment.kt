package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentAddModeBinding
import com.example.smartlamp.model.ModeModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddModeFragment: Fragment() {
    private lateinit var binding: FragmentAddModeBinding

    private var flicker = 0
    private var brightness = 50F
    private var name = "Custom Mode"
    private var edit = false

    private val viewModel: LampViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentAddModeBinding.inflate(layoutInflater)

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                brightness = slider.value

            }
        })

        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }


        binding.swFlicker.setOnCheckedChangeListener { _, isChecked ->
            flicker = if (isChecked)
                1
            else
                0
        }

        binding.etName.isEnabled = edit
        binding.ivEdit.setOnClickListener{
            edit = !edit
            binding.etName.isEnabled = edit
            if(edit) {
                binding.ivEdit.setImageResource(R.drawable.ic_check)
            } else {
                binding.ivEdit.setImageResource(R.drawable.ic_edit)
                name = binding.etName.text.toString()
            }
        }

        binding.tvDone.setOnClickListener{
            viewModel.saveMode(ModeModel(brightness, R.drawable.falling_star,name, flicker, false))
            findNavController().navigate(R.id.navigation_light_mode)
            viewModel.getAllModes()
        }

        setUI()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        binding.slider.value = brightness
        binding.swFlicker.isChecked = flicker == 1
    }

}
