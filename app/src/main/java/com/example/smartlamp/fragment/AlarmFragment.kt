package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.AlarmAdapter
import com.example.smartlamp.databinding.FragmentAlarmBinding
import com.example.smartlamp.model.AlarmModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : Fragment(), AlarmAdapter.SwitchClickInterface, AlarmAdapter.AlarmClickInterface {
    lateinit var binding: FragmentAlarmBinding

    private val alarm1 = AlarmModel("5:30", true, arrayListOf(1,1,1,0,1,1,1,0))

    private val alarms = listOf(alarm1)

    private lateinit var alarmAdapter: AlarmAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setUI()

        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        alarmAdapter = AlarmAdapter(requireContext(),alarms,this,this)

        binding.rvAlarm.apply {
            adapter = alarmAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        alarmAdapter.notifyDataSetChanged()
    }

    override fun onAlarmClick(alarm: AlarmModel) {
        findNavController().navigate(R.id.navigation_alarm_setting)
    }

    override fun onSwitchClick(position: Int, isChecked: Boolean) {

    }

}