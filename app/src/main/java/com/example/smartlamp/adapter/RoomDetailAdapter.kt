//package com.example.smartlamp.adapter
//
//import android.annotation.SuppressLint
//import android.content.ClipData
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.appcompat.widget.SwitchCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.example.smartlamp.R
//import com.example.smartlamp.databinding.ItemDeviceBinding
//import com.example.smartlamp.model.DeviceModel
//import com.google.android.material.slider.Slider
//
//
//class RoomDetailAdapter(
//    var context: Context,
//    var devices: List<DeviceModel>,
//    private val onSwitchClickInterface: SwitchClickInterface,
//    private val onDeviceClickInterface: DeviceClickInterface,
//    private val onStopTrackingTouchListener: OnStopTrackingTouchListener,
//    ) : RecyclerView.Adapter<RoomDetailAdapter.ViewHolder>() {
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    inner class ViewHolder(var binding: ItemDeviceBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            ItemDeviceBinding.inflate(
//                LayoutInflater.from(context),
//                parent,
//                false
//            )
//        )
//    }
//
//    @SuppressLint("SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables")
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = devices[position]
//        val bind = holder.binding
////
////        if (item.state == 1){
////            bind.swDevice.isChecked = true
////            bind.ivDevice.setImageResource(R.drawable.lamp_on)
////        } else {
////            bind.swDevice.isChecked = false
////            bind.ivDevice.setImageResource(R.drawable.lamp)
////        }
////
////        bind.tvDevice.text = "Lamp"
////
////        bind.swDevice.setOnCheckedChangeListener { _, isChecked ->
////            onSwitchClickInterface.onSwitchClick(position, isChecked)
////            if (isChecked){
////                bind.ivDevice.setImageResource(R.drawable.lamp_on)
////            } else {
////                bind.ivDevice.setImageResource(R.drawable.lamp)
////            }
////        }
//
//        holder.itemView.setOnClickListener {
//            onDeviceClickInterface.onDeviceClick(item)
//        }
//
//        bind.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
//            override fun onStartTrackingTouch(slider: Slider) {
//                // Không làm gì khi bắt đầu kéo
//            }
//
//            override fun onStopTrackingTouch(slider: Slider) {
//                val value = slider.value
//                onStopTrackingTouchListener.onStopTrackingTouch(value)
//            }
//        })
//
//        bind.slider.value = item.brightness
//
//    }
//
//
//    override fun getItemCount(): Int {
//        return devices.size
//    }
//
//    interface SwitchClickInterface {
//        fun onSwitchClick(position: Int, isChecked: Boolean)
//    }
//
//    interface DeviceClickInterface {
//        fun onDeviceClick(device: DeviceModel)
//    }
//
//    interface OnStopTrackingTouchListener {
//        fun onStopTrackingTouch(value: Float)
//    }
//
//}