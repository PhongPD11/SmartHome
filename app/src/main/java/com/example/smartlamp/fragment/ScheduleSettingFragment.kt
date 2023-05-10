package com.example.smartlamp.fragment

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.databinding.DialogRepeatBinding
import com.example.smartlamp.databinding.DialogRepeatCustomBinding
import com.example.smartlamp.databinding.FragmentScheduleSettingBinding
import com.example.smartlamp.model.ScheduleModel
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

    var repeatDisplay = ""
    var repeat = ArrayList<Int>()
    private var key = ""
    var hourOn = 0
    var hourOff = 0
    var minOn = 0
    var minOff = 0

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    var swOn = true

    @RequiresApi(Build.VERSION_CODES.M)
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

                    binding.timePicker.hour = hourOn
                    binding.timePicker.minute = minOn
                }
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
        }

        binding.timePicker.setIs24HourView(true)

        updateRepeat()

        return binding.root
    }

    private fun showDialog(context: Context) {
        val dialog = BottomSheetDialog(context)
        bindingDialog = DialogRepeatBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes

        when(repeatDisplay){
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
            repeatDisplay = RepeatDisplay.repeatDisplay(repeat)
            updateRepeat()
        }
        bindingDialog.consDaily.setOnClickListener {
            dialog.dismiss()
            repeat = arrayListOf(1,1,1,1,1,1,1,0)
            repeatDisplay = RepeatDisplay.repeatDisplay(repeat)
            updateRepeat()
        }

        bindingDialog.consCustom.setOnClickListener {
            dialog.dismiss()
            showCustomDialog(context)
            updateRepeat()
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }

    private fun updateRepeat(){
        binding.tvRepeat.text = repeatDisplay
        binding.tvRepeatTime.text = repeatDisplay
    }

    private fun showCustomDialog(context: Context) {
        val dialog = BottomSheetDialog(context)
        bindingDialogCustom = DialogRepeatCustomBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogCustom.root)
        val window = dialog.window
        val params = window?.attributes

        bindingDialogCustom.btnCancel.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        dialog.show()
    }

    private fun saveSetting() {
        val newState = if (swOn) 1 else 0
        ledNodeRef.child("schedule/$key/state").setValue(newState)
        ledNodeRef.child("schedule/$key/repeat").setValue(repeat)
    }

}