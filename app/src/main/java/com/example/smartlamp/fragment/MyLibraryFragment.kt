package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentMyLibraryBinding
import com.example.smartlamp.databinding.FragmentProfileSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MyLibraryFragment : Fragment() {
    private lateinit var binding: FragmentMyLibraryBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentMyLibraryBinding.inflate(layoutInflater)

        binding.cardSchedule.setOnClickListener {
            findNavController().navigate(R.id.navigation_schedule)
        }

        binding.cardBookshelf.setOnClickListener {
            findNavController().navigate(R.id.navigation_book_shelf)
        }

        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.navigation_contact)
        }

        return binding.root
    }

}