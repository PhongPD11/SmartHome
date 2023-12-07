package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.CustomChooseAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogRepeatBinding
import com.example.smartlamp.databinding.DialogRepeatCustomBinding
import com.example.smartlamp.databinding.FragmentScheduleSettingBinding
import com.example.smartlamp.model.CustomChooseModel
import com.example.smartlamp.model.ScheduleResponse
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.ID
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.ScheduleViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleSettingFragment : Fragment() {
    lateinit var binding: FragmentScheduleSettingBinding

    private lateinit var customChooseAdapter: CustomChooseAdapter

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    private val scheduleViewmodel : ScheduleViewModel by activityViewModels()

    var selectedSchedule: ScheduleResponse.ScheduleData? = null

    private var repeatDisplay = ""
    private var repeat = ""
    private var isAdd = false
    private var hourOn = 0
    private var minOn = 0
    private var typeRepeat = "Once"
    private val daysOfWeek = arrayOf( "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    private var days = ArrayList<CustomChooseModel>()
    private var newPosition = ""
    private var id = 0

    private var swOn = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleSettingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        isAdd = (arguments?.getString("add_schedule")!!) == "add"

        if (isAdd) {
            hourOn = 7
            minOn = 0
            repeat = "0000000"
            repeatDisplay = "Once"
            binding.tvTime.text = Utils.updateTime(hourOn,minOn)
            binding.swDevice.isChecked = true
            newPosition = arguments?.getString("add_schedule")!!
            updateRepeat(repeatDisplay)
            updateDays(days, repeat)
        } else {
            selectedSchedule = arguments?.get(Constants.SELECTED_SCHEDULE) as ScheduleResponse.ScheduleData?
            if (selectedSchedule != null) {
                hourOn = selectedSchedule!!.hourTime
                minOn = selectedSchedule!!.minuteTime
                repeat = selectedSchedule!!.repeat
                repeatDisplay = selectedSchedule!!.typeRepeat
                typeRepeat = selectedSchedule!!.typeRepeat
                binding.tvTime.text = Utils.updateTime(hourOn,minOn)
                binding.swDevice.isChecked = selectedSchedule!!.isOn
                if (typeRepeat == "Custom") {
                    updateRepeat(repeat)
                } else {
                    updateRepeat(repeatDisplay)
                }
                updateDays(days, repeat)
                id = selectedSchedule!!.id
            }
        }

        binding.consRepeat.setOnClickListener {
            showDialog(requireContext())
        }

        binding.swDevice.setOnCheckedChangeListener { _, isChecked ->
            swOn = isChecked
        }

        binding.timePicker.hour = hourOn
        binding.timePicker.minute = minOn

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            hourOn = hourOfDay
            minOn = minute
        }

        binding.btnSetTime.setOnClickListener {
            saveSetting()
        }

        binding.timePicker.setIs24HourView(true)

        return binding.root
    }

    private fun updateDays(days: ArrayList<CustomChooseModel>, repeat: String){
        days.clear()
        for(i in daysOfWeek.indices){
            val day = CustomChooseModel(daysOfWeek[i],repeat[i])
            days.add(day)
        }
    }

    private fun showDialog(context: Context) {
        val dialog = BottomSheetDialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogRepeatBinding = DialogRepeatBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes

        when(typeRepeat){
            "Once" -> {
                bindingDialog.tvOnce.setTextColor(Color.parseColor("#60A34A"))
                bindingDialog.consOnce.setBackgroundColor(Color.parseColor("#E5F1E2"))
            }
            "Daily" -> {
                bindingDialog.tvDaily.setTextColor(Color.parseColor("#60A34A"))
                bindingDialog.consDaily.setBackgroundColor(Color.parseColor("#E5F1E2"))
            }
            else -> {
                bindingDialog.tvCustom.setTextColor(Color.parseColor("#60A34A"))
                bindingDialog.consCustom.setBackgroundColor(Color.parseColor("#E5F1E2"))
            }
        }

        bindingDialog.consOnce.setOnClickListener {
            dialog.dismiss()
            updateRepeat("Once")
            typeRepeat = "Once"
        }
        bindingDialog.consDaily.setOnClickListener {
            dialog.dismiss()
            updateRepeat("Daily")
            typeRepeat = "Daily"
        }

        bindingDialog.consCustom.setOnClickListener {
            dialog.dismiss()
            showCustomDialog(context)
            typeRepeat = "Custom"
            updateRepeat(Utils.repeatDisplay(repeat))
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }


    private fun updateRepeat(repeat : String){
        binding.tvRepeat.text = Utils.repeatDisplay(repeat)
        binding.tvRepeatTime.text = Utils.repeatDisplay(repeat)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCustomDialog(context: Context) {
        var repeatCustom = repeat
        val daysCustom = arrayListOf<CustomChooseModel>()
        daysCustom.addAll(days)

        val dialog = BottomSheetDialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogRepeatCustomBinding = DialogRepeatCustomBinding.inflate(layoutInflater)

        customChooseAdapter = CustomChooseAdapter(context, daysCustom, object : CustomChooseAdapter.DayClickInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDayClick(position: Int) {
                val charArray = repeatCustom.toCharArray()
                charArray[position] = toggleState(charArray[position])
                repeatCustom = String(charArray)
                updateDays(daysCustom, repeatCustom)
                customChooseAdapter.notifyDataSetChanged()
            }
        })
        bindingDialog.rvChoose.apply{
            adapter = customChooseAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        customChooseAdapter.notifyDataSetChanged()

        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes


        bindingDialog.btnCancel.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }

        bindingDialog.btnDone.setOnClickListener {
            repeat = repeatCustom
            days.clear()
            days.addAll(daysCustom)
            updateRepeat(repeat)
            dialog.dismiss()
        }

        params?.gravity = Gravity.BOTTOM
        dialog.show()
    }

    private fun toggleState(state: Char): Char {
        return if (state == '1') '0' else '1'
    }

    private fun saveSetting() {
        val uid = sharedPref.getInt(Constants.UID)
        val map : HashMap<String, Any> = hashMapOf(
            Constants.UID to uid,
            Constants.HOUR_TIME to hourOn,
            Constants.MINUTE_TIME to minOn,
            Constants.IS_ON to swOn,
            Constants.REPEAT to repeat,
            Constants.TYPE_REPEAT to typeRepeat
        )
        if(isAdd){
            apiInterface.setSchedule(map).enqueue(object : Callback<SimpleApiResponse> {
                override fun onResponse(call: Call<SimpleApiResponse>, response: Response<SimpleApiResponse>) {
                    if (response.body()?.code == 200){
                        findNavController().popBackStack()
                        scheduleViewmodel.getSchedule(uid)
                    }
                }

                override fun onFailure(p0: Call<SimpleApiResponse>, p1: Throwable) {
                }
            })
        } else {
            map[ID] = id
            apiInterface.updateSchedule(map).enqueue(object : Callback<SimpleApiResponse> {
                override fun onResponse(call: Call<SimpleApiResponse>, response: Response<SimpleApiResponse>) {
                    if (response.body()?.code == 200){
                        findNavController().popBackStack()
                        scheduleViewmodel.getSchedule(uid)
                    }
                }

                override fun onFailure(p0: Call<SimpleApiResponse>, p1: Throwable) {
                }
            })
        }
    }

}