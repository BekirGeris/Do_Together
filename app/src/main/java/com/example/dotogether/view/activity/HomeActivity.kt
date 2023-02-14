package com.example.dotogether.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.work.*
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityHomeBinding
import com.example.dotogether.util.Constants
import com.example.dotogether.view.callback.ConfirmDialogListener
import com.example.dotogether.view.fragment.*
import com.example.dotogether.workers.NotificationWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

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
                    if (activeFragment is HomeFragment) {
                        activeFragment.goToRecyclerViewTop()
                    }
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
                    if (activeFragment is ProfileFragment) {
                        activeFragment.goToRecyclerViewTop()
                    }
                    activeFragment = profileFragment
                    true
                }
                else -> false
            }
        }

        val workManager = WorkManager.getInstance(applicationContext)

        workManager.getWorkInfosByTagLiveData(Constants.TAG_NOTIFICATION_WORKER).observe(this) {
            if (it == null || it.isEmpty()) {
                // No work with the given tag
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 10)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                if (calendar.timeInMillis - System.currentTimeMillis() <= 0) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }

                val repeatingWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                    .setInitialDelay(calendar.timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addTag(Constants.TAG_NOTIFICATION_WORKER)
                    .build()

                workManager.enqueue(repeatingWorkRequest)
            }
        }

        this.onBackPressedDispatcher.addCallback {
            showAlertDialog("Uygulama Kapatılacak!", object : ConfirmDialogListener {
                override fun cancel() {

                }

                override fun confirm() {
                    this@HomeActivity.finish()
                }
            })
        }
    }

    fun onNavigationItemSelected(item: MenuItem) {
        when(item.itemId) {
            R.id.navigation_home -> {

            }
            R.id.navigation_discover -> {

            }
            R.id.navigation_share -> {
                homeFragment.actionOnClick(1)
            }
            R.id.navigation_library -> {

            }
            R.id.navigation_profile -> {

            }
        }
    }
}