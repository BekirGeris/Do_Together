package com.example.dotogether.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityHomeBinding
import com.example.dotogether.util.Constants.ViewType
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
        navController = findNavController(R.id.homeFragmentContainerView)
        NavigationUI.setupWithNavController(navView, navController)
    }

    fun onNavigationItemSelected(item: MenuItem) {
        when(item.itemId) {
            R.id.navigation_home -> {

            }
            R.id.navigation_search -> {

            }
            R.id.navigation_share -> {
                navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_SHARE_FRAGMENT))
            }
            R.id.navigation_library -> {

            }
            R.id.navigation_profile -> {

            }
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}