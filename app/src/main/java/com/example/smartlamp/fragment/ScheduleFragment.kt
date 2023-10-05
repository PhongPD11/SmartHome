package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.ScheduleAdapter
import com.example.smartlamp.databinding.FragmentScheduleBinding
import com.example.smartlamp.model.ScheduleModel
import com.example.smartlamp.utils.Utils
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment(), ScheduleAdapter.SwitchClickInterface,
    ScheduleAdapter.ScheduleClickInterface {
    lateinit var binding: FragmentScheduleBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    private val schedules = ArrayList<ScheduleModel>()
    private var keyList = ArrayList<String>()

    private lateinit var scheduleAdapter: ScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)

        binding.fabAdd.setOnClickListener {
            val bundle = bundleOf("add_schedule" to (keyList.size+1).toString())
            findNavController().navigate(R.id.navigation_schedule_setting, bundle)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        setUI()
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        scheduleAdapter = ScheduleAdapter(requireContext(), schedules, this, this)

        binding.rvSchedule.apply {
            adapter = scheduleAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        scheduleAdapter.notifyDataSetChanged()
    }

    override fun onScheduleClick(schedule: ScheduleModel, repeat: String, switch: Boolean, position: Int) {
        val time = Utils.updateTime(schedule.hour,schedule.min)
        val bundle = bundleOf("time" to time, "repeat" to repeat, "sw_device" to switch, "schedule" to keyList[position])
        findNavController().navigate(R.id.navigation_schedule_setting, bundle)
    }

    override fun onSwitchClick(position: Int, isChecked: Boolean) {
        val newState = if (isChecked) 1 else 0
        ledNodeRef.child("schedule/${keyList[position]}/state").setValue(newState)
    }

}