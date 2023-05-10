package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.CustomChooseAdapter
import com.example.smartlamp.adapter.ScheduleAdapter
import com.example.smartlamp.databinding.DialogRepeatBinding
import com.example.smartlamp.databinding.DialogRepeatCustomBinding
import com.example.smartlamp.databinding.FragmentScheduleSettingBinding
import com.example.smartlamp.model.CustomChooseModel
import com.example.smartlamp.utils.RepeatDisplay
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleSettingFragment : Fragment() {
    lateinit var binding: FragmentScheduleSettingBinding
    lateinit var bindingDialog: DialogRepeatBinding
    lateinit var bindingDialogCustom: DialogRepeatCustomBinding

    private val viewModel: LampViewModel by activityViewModels()

    private lateinit var customChooseAdapter: CustomChooseAdapter

    var repeatDisplay = ""
    var repeat = ArrayList<Int>()
    private var key = ""
    var hourOn = 0
    var hourOff = 0
    var minOn = 0
    var minOff = 0
    private val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var days = ArrayList<CustomChooseModel>()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    var swOn = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleSettingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        repeatDisplay = arguments?.getString("repeat")!!

        key = arguments?.getString("schedule")!!
        binding.tvTime.text = arguments?.getString("time")!!
        binding.swDevice.isChecked = arguments?.getBoolean("sw_device")!!

        bindingDialog = DialogRepeatBinding.inflate(layoutInflater)

        viewModel.getLampData().observe(viewLifecycleOwner) {
            if (it != null) {
                repeat = it.schedule[key]!!.repeat
                val time = it.schedule[key]?.time
                if (time != null) {
                    hourOn = time.hourOn
                    hourOff = time.hourOff
                    minOn = time.minOn
                    minOff = time.minOff

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.timePicker.hour = hourOn
                        binding.timePicker.minute = minOn
                    }
                }
                updateDays(days, repeat)
            }
        }

        binding.consRepeat.setOnClickListener {
            showDialog(requireContext())
        }

        binding.swDevice.setOnCheckedChangeListener { _, isChecked ->
            swOn = isChecked
        }

        binding.timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hourOn = hourOfDay
            minOn = minute
        }

        binding.btnSetTime.setOnClickListener {
            saveSetting()
            Handler().postDelayed({
                findNavController().popBackStack()
            },500)
        }

        binding.timePicker.setIs24HourView(true)

        updateRepeat(repeatDisplay)

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
        bindingDialog = DialogRepeatBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes

        when(RepeatDisplay.repeatDisplay(repeat)){
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
            updateRepeat(RepeatDisplay.repeatDisplay(repeat))
        }
        bindingDialog.consDaily.setOnClickListener {
            dialog.dismiss()
            repeat = arrayListOf(1,1,1,1,1,1,1,0)
            updateRepeat(RepeatDisplay.repeatDisplay(repeat))
        }

        bindingDialog.consCustom.setOnClickListener {
            dialog.dismiss()
            showCustomDialog(context)
            updateRepeat(RepeatDisplay.repeatDisplay(repeat))
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }


    private fun updateRepeat(repeat : String){
        binding.tvRepeat.text = repeat
        binding.tvRepeatTime.text = repeat
    }

    private fun showCustomDialog(context: Context) {
        val repeatCustom = arrayListOf<Int>()
        val daysCustom = arrayListOf<CustomChooseModel>()
        daysCustom.addAll(days)
        if (RepeatDisplay.repeatDisplay(repeat) == "Daily"){
            for (i in daysCustom.indices){
                repeatCustom.add(daysCustom[i].state)
            }
            repeatCustom.add(0)
        } else {
            repeatCustom.addAll(repeat)
        }


        val dialog = BottomSheetDialog(context, R.style.CustomDialogTheme)
        bindingDialogCustom = DialogRepeatCustomBinding.inflate(layoutInflater)

        customChooseAdapter = CustomChooseAdapter(context, daysCustom, object : CustomChooseAdapter.DayClickInterface {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDayClick(position: Int) {
                repeatCustom[position] = toggleState(repeatCustom[position])
                updateDays(daysCustom, repeatCustom)
                customChooseAdapter.notifyDataSetChanged()
            }
        })
        bindingDialogCustom.rvChoose.apply{
            adapter = customChooseAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        customChooseAdapter.notifyDataSetChanged()

        dialog.setContentView(bindingDialogCustom.root)
        val window = dialog.window
        val params = window?.attributes


        bindingDialogCustom.btnCancel.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }

        bindingDialogCustom.btnDone.setOnClickListener {
            repeat.clear()
            repeat.addAll(repeatCustom)
            repeat[7] = 0
            days.clear()
            days.addAll(daysCustom)
            updateRepeat(RepeatDisplay.repeatDisplay(repeat))
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
        val newState = if (swOn) 1 else 0
        ledNodeRef.child("schedule/$key/state").setValue(newState)
        ledNodeRef.child("schedule/$key/repeat").setValue(repeat)
        ledNodeRef.child("schedule/$key/time/hourOn").setValue(hourOn)
        ledNodeRef.child("schedule/$key/time/minOn").setValue(minOn)
    }

}