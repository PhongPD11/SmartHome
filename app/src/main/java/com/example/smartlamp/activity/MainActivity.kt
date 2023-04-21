package com.example.smartlamp.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener,
    NavController.OnDestinationChangedListener {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.mobile_navigation)

        navView.inflateMenu(R.menu.bottom_nav_menu)
        navGraph.setStartDestination(R.id.navigation_home)
        navController.graph = navGraph

        navController.addOnDestinationChangedListener(this)
        navView.setOnItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.navigation_home -> {
                navController.navigate(R.id.navigation_home)
                true
            }
            R.id.navigation_statistic -> {
                navController.navigate(R.id.navigation_statistic)
                true
            }
            R.id.navigation_user -> {
                navController.navigate(R.id.navigation_user)
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
            destination.id == R.id.navigation_statistic ||
            destination.id == R.id.navigation_user
        ) {
            binding.navView.visibility = View.VISIBLE
        }
    }
}