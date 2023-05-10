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
import com.example.smartlamp.adapter.RoomDetailAdapter
import com.example.smartlamp.databinding.FragmentAlarmBinding
import com.example.smartlamp.model.AlarmModel
import com.example.smartlamp.utils.RecyclerTouchListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : Fragment(), RoomDetailAdapter.OnSwitchCompatClickListener {
    lateinit var binding: FragmentAlarmBinding

    private val alarm1 = AlarmModel("5:30", true, listOf("Wed","Sar"))

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

        binding.rvAlarm.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvAlarm,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )

        setUI()
        alarmAdapter

        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        alarmAdapter = AlarmAdapter(requireContext(),alarms)

        binding.rvAlarm.apply {
            adapter = alarmAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        alarmAdapter.notifyDataSetChanged()
    }

    override fun onSwitchCompatClick(position: Int, isChecked: Boolean) {
        val item = alarms[position]
        item.button = isChecked

        binding.rvAlarm.post {
            alarmAdapter.notifyItemChanged(position)
        }
    }
}