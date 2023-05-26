package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.activity.MainActivity
import com.example.smartlamp.activity.SplashscreenActivity
import com.example.smartlamp.adapter.RoomAdapter
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.DialogYesNoBinding
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.databinding.FragmentProfileBinding
import com.example.smartlamp.model.DailyForecast
import com.example.smartlamp.model.RoomModel
import com.example.smartlamp.utils.Constants.NO_CLOUD
import com.example.smartlamp.utils.Constants.PICK_IMAGE
import com.example.smartlamp.utils.Constants.SMALL_SUN
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.viewmodel.HomeViewModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: LampViewModel by activityViewModels()

    private var userName = ""
    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)

        viewModel.getUserData().observe(viewLifecycleOwner){
            if (it != null) {
                binding.tvName.text = it.firstName +" "+ it.lastName
            }
        }

        binding.cardLogout.setOnClickListener {
            showDialog(requireContext())
        }

        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.navigation_profile_setting)
        }

        setObserb()
        setUI()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setObserb() {
        binding.tvName.text = userName
    }

    @SuppressLint("SetTextI18n")
    private fun showDialog(context: Context) {
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogYesNoBinding = DialogYesNoBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)

        val window = dialog.window
        val params = window?.attributes

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        bindingDialog.btnCancel.setOnClickListener{
            dialog.dismiss()
        }

        bindingDialog.btnYes.text = "Yes"
        bindingDialog.tvTitle.text = getString(R.string.log_out)

        bindingDialog.btnYes.setOnClickListener{
            dialog.dismiss()
            auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

private fun setUI() {}

}