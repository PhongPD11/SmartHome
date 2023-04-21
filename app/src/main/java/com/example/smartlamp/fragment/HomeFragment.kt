package com.example.smartlamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.model.RoomModel

class HomeFragment: Fragment() {
    lateinit var binding: FragmentHomeBinding
    var rooms = ArrayList<RoomModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }


}