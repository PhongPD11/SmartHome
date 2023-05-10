package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.adapter.RoomDetailAdapter
import com.example.smartlamp.databinding.FragmentRoomBinding
import com.example.smartlamp.model.DeviceModel
import com.example.smartlamp.utils.RecyclerTouchListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment : Fragment(), RoomDetailAdapter.TrackChangeInterface, RoomDetailAdapter.SwitchClickInterface, RoomDetailAdapter.DeviceClickInterface {
    lateinit var binding: FragmentRoomBinding

    private val device1 = DeviceModel(R.drawable.lamp, "Lamp", false, 20f)
    private val device2 = DeviceModel(R.drawable.lamp_on, "Lamp", true, 20f)
    private val devices = listOf(device1, device2)

    private lateinit var deviceAdapter: RoomDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setUI()
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        deviceAdapter = RoomDetailAdapter(requireContext(),devices, this, this, this)

        binding.rvDevice.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        deviceAdapter.notifyDataSetChanged()
    }

    override fun onSwitchClick(position: Int, isChecked: Boolean) {
        Toast.makeText(requireContext(), "$isChecked", Toast.LENGTH_SHORT).show()
    }

    override fun onDeviceClick(device: DeviceModel) {
        findNavController().navigate(R.id.navigation_alarm)
    }

    override fun onTrackChange(value: Float) {

    }
}