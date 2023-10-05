package com.example.smartlamp.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ActivityMainBinding
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Urls.LOGIN
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener,
    NavController.OnDestinationChangedListener {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navView: BottomNavigationView

    private var name = ""

    @Inject
    lateinit var sharedPref: SharedPref

    private var signed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        sharedPref = SharedPref(this)
        signed = sharedPref.getBoolean(LOGIN)

        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.mobile_navigation)

        navView.inflateMenu(R.menu.bottom_nav_menu)
        navGraph.setStartDestination(R.id.navigation_home)
        navController.graph = navGraph

        navController.addOnDestinationChangedListener(this)
        navView.setOnItemSelectedListener(this)

        val badge = navView.getOrCreateBadge(R.id.navigation_notifications)
        badge.isVisible = true

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.navigation_home -> {
                val bundle = bundleOf("signed" to signed, "name" to name )
                navController.navigate(R.id.navigation_home, bundle)
                true
            }
            R.id.navigation_library -> {
//                navController.navigate(R.id.navigation_statistic)
                true
            }
            R.id.navigation_notifications -> {
//                navController.navigate(R.id.navigation_statistic)
                true
            }
            R.id.navigation_user -> {
                if(signed){
                    navController.navigate(R.id.navigation_profile)
                } else {
                    navController.navigate(R.id.navigation_auth)
                }
                true
            }
            else -> {
                true
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (
            destination.id == R.id.navigation_home ||
            destination.id == R.id.navigation_library ||
            destination.id == R.id.navigation_user
        ) {
            binding.navView.visibility = View.VISIBLE
        }
    }
}