package com.example.fitnessapp.ui.Fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.others.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToTrackingFragmentIfNeeded(intent)

        val toolbar : MaterialToolbar = findViewById(R.id.toolbar)
        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)as NavHost

         navController = navHostFragment.navController



        bottomNavigationView.setupWithNavController(navHostFragment.navController)
        bottomNavigationView.setOnNavigationItemReselectedListener { /** No Oper  */}

        navHostFragment.navController.addOnDestinationChangedListener{_,destinaton,_->

            when(destinaton.id){
                R.id.settingFragment,R.id.runFragment,R.id.statisticsFragment->{
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else-> bottomNavigationView.visibility = View.GONE
            }
        }
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?){

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)as NavHost

        navController = navHostFragment.navController

        if(intent?.action == ACTION_SHOW_TRACKING_FRAGMENT){

            navHostFragment.navController.navigate(R.id.action_global_trackingFragment)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

       navigateToTrackingFragmentIfNeeded(intent)
    }
}