package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.CustomChooseAdapter
import com.example.smartlamp.databinding.DialogRepeatBinding
import com.example.smartlamp.databinding.DialogRepeatCustomBinding
import com.example.smartlamp.databinding.DialogTimeOffBinding
import com.example.smartlamp.databinding.FragmentScheduleSettingBinding
import com.example.smartlamp.model.CustomChooseModel
import com.example.smartlamp.model.Schedule
import com.example.smartlamp.model.Time
import com.example.smartlamp.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class ScheduleSettingFragment : Fragment() {
    lateinit var binding: FragmentScheduleSettingBinding

    private lateinit var customChooseAdapter: CustomChooseAdapter

    private var repeatDisplay = ""
    private var repeat = ArrayList<Int>()
    private var key = ""
    private var hourOn = 0
    private var hourOff = 0
    private var minOn = 0
    private var minOff = 0
    private val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    private var days = ArrayList<CustomChooseModel>()
    private var newPosition = ""

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    private var swOn = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleSettingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        repeatDisplay = arguments?.getString("repeat")!!
        key = arguments?.getString("schedule")!!

        if (key != ""){
            binding.tvTime.text = arguments?.getString("time")!!
            binding.swDevice.isChecked = arguments?.getBoolean("sw_device")!!
        } else {
            hourOn = 7
            hourOff = 9
            minOn = 0
            minOff = 0
            repeat = arrayListOf(0,0,0,0,0,0,0,1)
            repeatDisplay = "Once"
            binding.tvTime.text = Utils.updateTime(hourOn,minOn)
            binding.swDevice.isChecked = true
            newPosition = arguments?.getString("add_schedule")!!
            updateRepeat(repeatDisplay)
            updateTimeOff()
            updateDays(days, repeat)
        }

        binding.consRepeat.setOnClickListener {
            showDialog(requireContext())
        }

        binding.swDevice.setOnCheckedChangeListener { _, isChecked ->
            swOn = isChecked
        }

        binding.timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            hourOn = hourOfDay
            minOn = minute
        }

        binding.consTimeOff.setOnClickListener {
            showTimeOffDialog(requireContext())
        }

        binding.btnSetTime.setOnClickListener {
            saveSetting()
            Handler().postDelayed({
                findNavController().popBackStack()
            },500)
        }

        binding.timePicker.setIs24HourView(true)

        return binding.root
    }

    private fun updateDays( days: ArrayList<CustomChooseModel>, repeat: ArrayList<Int>){
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

        when(Utils.repeatDisplay(repeat)){
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
            repeat[7]=1
            updateRepeat(Utils.repeatDisplay(repeat))
        }
        bindingDialog.consDaily.setOnClickListener {
            dialog.dismiss()
            repeat = arrayListOf(1,1,1,1,1,1,1,0)
            updateRepeat(Utils.repeatDisplay(repeat))
        }

        bindingDialog.consCustom.setOnClickListener {
            dialog.dismiss()
            showCustomDialog(context)
            updateRepeat(Utils.repeatDisplay(repeat))
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }

    private fun updateTimeOff(){
        binding.tvTimeOff.text = Utils.updateTime(hourOff,minOff)
    }

    private fun updateRepeat(repeat : String){
        binding.tvRepeat.text = repeat
        binding.tvRepeatTime.text = repeat
    }


    @OptIn(InternalCoroutinesApi::class)
    private fun showTimeOffDialog(context: Context){
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogTimeOffBinding = DialogTimeOffBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes
        var pickHourOff = 0
        var pickMinOff = 0

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bindingDialog.timeOffPicker.hour = hourOff
            bindingDialog.timeOffPicker.minute = minOff
        }

        bindingDialog.timeOffPicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            pickHourOff = hourOfDay
            pickMinOff = minute
        }

        bindingDialog.btnDone.setOnClickListener {
            dialog.dismiss()
            hourOff = pickHourOff
            minOff = pickMinOff
            updateTimeOff()
        }

        bindingDialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        bindingDialog.timeOffPicker.setIs24HourView(true)

        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showCustomDialog(context: Context) {
        val repeatCustom = arrayListOf<Int>()
        val daysCustom = arrayListOf<CustomChooseModel>()
        daysCustom.addAll(days)
        if (Utils.repeatDisplay(repeat) == "Daily"){
            for (i in daysCustom.indices){
                repeatCustom.add(daysCustom[i].state)
            }
            repeatCustom.add(0)
        } else {
            repeatCustom.addAll(repeat)
        }

        val dialog = BottomSheetDialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogRepeatCustomBinding = DialogRepeatCustomBinding.inflate(layoutInflater)

        customChooseAdapter = CustomChooseAdapter(context, daysCustom, object : CustomChooseAdapter.DayClickInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDayClick(position: Int) {
                repeatCustom[position] = toggleState(repeatCustom[position])
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
            repeat.clear()
            repeat.addAll(repeatCustom)
            repeat[7] = 0
            days.clear()
            days.addAll(daysCustom)
            updateRepeat(Utils.repeatDisplay(repeat))
            dialog.dismiss()
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }

    private fun toggleState(state: Int): Int {
        return if (state == 1) 0 else 1
    }

    private fun saveSetting() {
        if (key != ""){
            val newState = if (swOn) 1 else 0
            ledNodeRef.child("schedule/$key/state").setValue(newState)
            ledNodeRef.child("schedule/$key/repeat").setValue(repeat)
            ledNodeRef.child("schedule/$key/time/hourOn").setValue(hourOn)
            ledNodeRef.child("schedule/$key/time/minOn").setValue(minOn)
            ledNodeRef.child("schedule/$key/time/hourOff").setValue(hourOff)
            ledNodeRef.child("schedule/$key/time/minOff").setValue(minOff)
        } else {
            val schedule = Schedule(
                repeat = repeat,
                state = 1,
                time = Time(hourOff, hourOn, minOff, minOn)
            )
            ledNodeRef.child("schedule/schedule$newPosition").setValue(schedule)
        }
    }

}