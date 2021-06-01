package com.akiramenaide.capstoneproject.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akiramenaide.capstoneproject.R
import com.akiramenaide.capstoneproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
//        supportActionBar?.hide()
        //val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        //activityMainBinding.viewPager.adapter = sectionsPagerAdapter
        //activityMainBinding.tabs.setupWithViewPager(activityMainBinding.viewPager)

        val navView: BottomNavigationView = activityMainBinding.navView

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_analysis, R.id.navigation_profile
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.elevation = 0f

    }

}