package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.RoomAdapter
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.model.RoomModel
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by activityViewModels()

    private val livingRoom = RoomModel( R.drawable.living_room, "Living Room")
    private val bedroom = RoomModel( R.drawable.bed_room, "Bedroom")
    private val kitchenRoom = RoomModel( R.drawable.kitchen, "Kitchen")
    private val rooms = listOf(livingRoom, bedroom, kitchenRoom)
    private lateinit var roomAdapter: RoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        try {
            if(viewModel.test()){
                Toast.makeText(context, "True", Toast.LENGTH_SHORT).show()
            }
        } catch (e: java.lang.Exception){
            Log.d("viewmodel fault", e.message!!)
        }

        setUI()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI(){
        roomAdapter = RoomAdapter(requireContext(), rooms)
        binding.rvRoom.apply{
            adapter = roomAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        roomAdapter.notifyDataSetChanged()
    }

}