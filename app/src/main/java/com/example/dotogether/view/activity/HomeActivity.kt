package com.example.dotogether.view.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.work.*
import com.example.dotogether.R
import com.example.dotogether.alarms.NotificationAlarmReceiver
import com.example.dotogether.databinding.ActivityHomeBinding
import com.example.dotogether.util.Constants
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.helper.RuntimeHelper.TAG
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
                R.id.navigation_discover -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(discoverFragment).commit()
                    if (activeFragment is DiscoverFragment) {
                        activeFragment.goToRecyclerViewTop()
                    }
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

        this.onBackPressedDispatcher.addCallback {
            showAlertDialog("Uygulama Kapatılacak!", object : ConfirmDialogListener {
                override fun cancel() {

                }

                override fun confirm() {
                    this@HomeActivity.finish()
                }
            })
        }

        setAlarmManager()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun setWorkManager() {
        val workManager = WorkManager.getInstance(applicationContext)
        workManager.getWorkInfosByTagLiveData(Constants.TAG_NOTIFICATION_WORKER).observe(this) {
            if (it == null || it.isEmpty()) {
                // No work with the given tag

                val repeatingWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                    .setInitialDelay(getNotificationCalender().timeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addTag(Constants.TAG_NOTIFICATION_WORKER)
                    .build()

                workManager.enqueue(repeatingWorkRequest)
            }
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setAlarmManager() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val isAlarmSet = SharedPreferencesUtil.getBoolean(this, "isAlarmSet", false)
        if (!isAlarmSet) {
            Log.d(TAG, "setAlarmManager")
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                getNotificationCalender().timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            SharedPreferencesUtil.setBoolean(this, "isAlarmSet", true)
        }
    }

    private fun getNotificationCalender(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis - System.currentTimeMillis() <= 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar
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