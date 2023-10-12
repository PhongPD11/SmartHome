package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.smartlamp.R
import com.example.smartlamp.activity.MainActivity
import com.example.smartlamp.databinding.DialogYesNoBinding
import com.example.smartlamp.databinding.FragmentProfileBinding
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var sharedPref: SharedPref

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)

        sharedPref = SharedPref(context)
        binding.tvName.text = sharedPref.getString(FULL_NAME)
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)

        Glide.with(this).load(sharedPref.getString(Constants.IMAGE_URL)).apply(options).into(binding.ivAvatar)


        binding.cardLogout.setOnClickListener {
            showDialog(requireContext())
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.navigation_profile_setting)
        }

        setUI()

        return binding.root
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
            sharedPref.logoutApp()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

private fun setUI() {}

}