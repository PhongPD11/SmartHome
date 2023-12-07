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
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentScheduleBinding
import com.example.smartlamp.model.ScheduleResponse
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.ID
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.ScheduleViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : Fragment(), ScheduleAdapter.SwitchClickInterface,
    ScheduleAdapter.ScheduleClickInterface {
    lateinit var binding: FragmentScheduleBinding
    private val schedules = ArrayList<ScheduleResponse.ScheduleData>()

    private val scheduleViewmodel : ScheduleViewModel by activityViewModels()

    private lateinit var scheduleAdapter: ScheduleAdapter

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(layoutInflater)

        binding.fabAdd.setOnClickListener {
            val bundle = bundleOf("add_schedule" to "add")
            findNavController().navigate(R.id.navigation_schedule_setting, bundle)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        scheduleViewmodel.scheduleList.observe(viewLifecycleOwner){ scheduleList ->
            if (scheduleList != null) {
                schedules.clear()
                schedules.addAll(scheduleList)
                schedules.sortBy { it.id }
                setUI()
            }
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

    override fun onScheduleClick(position: Int) {
        val args = Bundle()
        args.putSerializable(Constants.SELECTED_SCHEDULE, schedules[position])
        findNavController().navigate(R.id.navigation_schedule_setting, args)
    }

    override fun onSwitchClick(position: Int, isChecked: Boolean) {
        val uid = sharedPref.getInt(Constants.UID)

        apiInterface.updateSchedule(hashMapOf(
            ID to schedules[position].id,
            Constants.UID to uid,
            Constants.HOUR_TIME to schedules[position].hourTime,
            Constants.MINUTE_TIME to schedules[position].minuteTime,
            Constants.IS_ON to isChecked,
            Constants.REPEAT to schedules[position].repeat,
            Constants.TYPE_REPEAT to schedules[position].typeRepeat
        )).enqueue(object : Callback<SimpleApiResponse> {
            override fun onResponse(call: Call<SimpleApiResponse>, response: Response<SimpleApiResponse>) {
                if (response.body()?.code == 200){
                    scheduleViewmodel.getSchedule(uid)
                }
            }
            override fun onFailure(p0: Call<SimpleApiResponse>, p1: Throwable) {

            }
        })
    }

}