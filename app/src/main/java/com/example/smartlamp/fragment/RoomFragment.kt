package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.RoomDetailAdapter
import com.example.smartlamp.databinding.DialogModeScheduleBinding
import com.example.smartlamp.databinding.FragmentRoomBinding
import com.example.smartlamp.model.DeviceModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomFragment : Fragment(), RoomDetailAdapter.TrackChangeInterface,
    RoomDetailAdapter.SwitchClickInterface, RoomDetailAdapter.DeviceClickInterface {
    lateinit var binding: FragmentRoomBinding

    private var room = ""

    private val devices = ArrayList<DeviceModel>()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    private lateinit var deviceAdapter: RoomDetailAdapter
    private val viewModel: LampViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomBinding.inflate(layoutInflater)

        room = arguments?.getString("name")!!
        binding.tvTitle.text = room

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (room == "Bedroom"){
            viewModel.getLampData().observe(viewLifecycleOwner) {
                val device = it?.state?.let { it1 -> DeviceModel(it1, it.brightness) }
                if (device != null) {
                    devices.clear()
                    devices.add(device)
                }
            }
        }

        setUI()
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        deviceAdapter = RoomDetailAdapter(
            requireContext(),
            devices, this,
            this,
            this
        )
        binding.rvDevice.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        deviceAdapter.notifyDataSetChanged()
    }

    private fun showModeOrSchedule(context: Context, state: Int, brightness: Float){
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogModeScheduleBinding = DialogModeScheduleBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes

        bindingDialog.cvMode.setOnClickListener {
            dialog.dismiss()
            val sw = (state == 1)
            val bundle = bundleOf("sw_lamp" to sw, "brightness" to brightness)
            findNavController().navigate(R.id.navigation_light_mode, bundle)
        }

        bindingDialog.cvSchedule.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_schedule)
        }

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        dialog.show()
    }

    override fun onSwitchClick(position: Int, isChecked: Boolean) {
        val newState = if (isChecked) 1 else 0
        ledNodeRef.child("state").setValue(newState)
    }

    override fun onDeviceClick(device: DeviceModel) {
        showModeOrSchedule(requireContext(), device.state, device.brightness)
    }

    override fun onTrackChange(value: Float) {
        ledNodeRef.child("brightness").setValue(value)
    }
}