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
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentProfileSettingBinding
import com.example.smartlamp.viewmodel.LampViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileSettingFragment : Fragment() {
    private lateinit var binding: FragmentProfileSettingBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileSettingBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.cardInfo.setOnClickListener {

        }

        binding.cardPass.setOnClickListener {
            findNavController().navigate(R.id.navigation_change_pass)
        }

        binding.cardInfo.setOnClickListener {
            findNavController().navigate(R.id.navigation_profile_information)
        }

        return binding.root
    }

}