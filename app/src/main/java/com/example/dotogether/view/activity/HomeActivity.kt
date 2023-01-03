package com.example.dotogether.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityHomeBinding
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.view.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController

    private val homeFragment = HomeFragment()
    private val discoverFragment = DiscoverFragment()
    private val libraryFragment = LibraryFragment()
    private val profileFragment = ProfileFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: BaseFragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navView = binding.navView
//        navController = findNavController(R.id.homeFragmentContainerView)
//        navController.popBackStack(HomeNavDirections.actionGlobalOthersActivity().actionId, true)
//        NavigationUI.setupWithNavController(navView, navController)

        fragmentManager.beginTransaction().apply {
            add(R.id.container, activeFragment)
            add(R.id.container, discoverFragment).hide(discoverFragment)
            add(R.id.container, libraryFragment).hide(libraryFragment)
            add(R.id.container, profileFragment).hide(profileFragment)
        }.commit()

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true
                }
                R.id.navigation_search -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(discoverFragment).commit()
                    activeFragment = discoverFragment
                    true
                }
                R.id.navigation_library -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(libraryFragment).commit()
                    activeFragment = libraryFragment
                    true
                }
                R.id.navigation_profile -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit()
                    activeFragment = profileFragment
                    true
                }
                else -> false
            }
        }
    }

    fun onNavigationItemSelected(item: MenuItem) {
        when(item.itemId) {
            R.id.navigation_home -> {

            }
            R.id.navigation_discover -> {

            }
            R.id.navigation_share -> {
                val intent = Intent(this, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_SHARE_FRAGMENT.type)
                startActivity(intent)
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