package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentProfileSettingBinding
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
            findNavController().navigate(R.id.navigation_edit_profile)
        }

        binding.cardPass.setOnClickListener {
            try {
                findNavController().navigate(R.id.navigation_change_pass)
            } catch (e:Exception){
                println(e.message)
            }
        }

        return binding.root
    }

}