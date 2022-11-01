package com.example.dotogether.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.*
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityOthersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersActivity : BaseActivity() {

    private lateinit var binding: ActivityOthersBinding
    private lateinit var navController: NavController
    private lateinit var navOptions: NavOptions

    private var viewType: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOthersBinding.inflate(layoutInflater)
        super.setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        navController = Navigation.findNavController(this, R.id.othersFragmentContainerView)
        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestinationId, true)
            .build()

        viewType = intent.extras?.getInt("viewType")

        startFragmentWithViewType()
    }

    private fun startFragmentWithViewType() {
        when(viewType) {
            0 -> {
                navController.navigate(OthersNavDirections.actionShareFragment(), navOptions)
            }
            1 -> {
                navController.navigate(OthersNavDirections.actionProfileFragment(), navOptions)
            }
            2 -> {
                navController.navigate(OthersNavDirections.actionListChatFragment(), navOptions)
            }
            3 -> {
                navController.navigate(OthersNavDirections.actionChatFragment(), navOptions)
            }
            4 -> {
                navController.navigate(OthersNavDirections.actionTargetFragment(), navOptions)
            }
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}