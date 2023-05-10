package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.adapter.AlarmAdapter
import com.example.smartlamp.databinding.FragmentAlarmBinding
import com.example.smartlamp.databinding.FragmentAlarmSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmSettingFragment : Fragment() {
    lateinit var binding: FragmentAlarmSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmSettingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.timePicker.setIs24HourView(true)

        return binding.root
    }

}