package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.RoomAdapter
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.DialogYesNoBinding
import com.example.smartlamp.databinding.FragmentHomeBinding
import com.example.smartlamp.databinding.FragmentProfileBinding
import com.example.smartlamp.model.DailyForecast
import com.example.smartlamp.model.RoomModel
import com.example.smartlamp.utils.Constants.NO_CLOUD
import com.example.smartlamp.utils.Constants.SMALL_SUN
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.viewmodel.HomeViewModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    var data = MutableLiveData<List<DailyForecast>>()
    private val viewModel: LampViewModel by activityViewModels()

    private var welcome = ""
    private var userName = ""
    private var signed = false
    private val auth = FirebaseAuth.getInstance()

    private lateinit var roomAdapter: RoomAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        viewModel.startObservingKey()

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            signed = true
            viewModel.uid.value = currentUser.uid
            viewModel.startObservingUser(currentUser.uid)
            viewModel.getUserData().observe(viewLifecycleOwner) {
                if (it != null) {
                    userName = it.firstName + " " + it.lastName
                }
            }
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
        bindingDialog.btnYes.setOnClickListener{
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_auth)
        }
        dialog.show()
    }

private fun setUI() {}

}