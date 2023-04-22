package com.example.smartlamp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentConsumptionBinding

class ConsumptionFragment : Fragment() {
    lateinit var binding: FragmentConsumptionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConsumptionBinding.inflate(layoutInflater)
        return binding.root
    }

}