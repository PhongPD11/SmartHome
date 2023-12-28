package com.example.smartlamp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentLockBinding
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener


@AndroidEntryPoint
class LockFragment : Fragment(), OnItemSingleClickListener {

    private lateinit var binding: FragmentLockBinding
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentLockBinding.inflate(layoutInflater)
        sharedPref = SharedPref(context)
        val singleClickListener = SingleClickListener(this)

        binding.btnContact.setOnClickListener(singleClickListener)
        binding.btnLogout.setOnClickListener(singleClickListener)

        return binding.root
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.btnContact -> {
                findNavController().navigate(R.id.navigation_contact)
            }

            R.id.btnLogout -> {
                sharedPref.logoutApp()
                findNavController().navigate(R.id.navigation_auth)
            }
        }
    }
}