package com.example.dotogether.view.activity

import android.os.Bundle
import androidx.navigation.*
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityOthersBinding
import com.example.dotogether.util.Constants.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersActivity : BaseActivity() {

    private lateinit var binding: ActivityOthersBinding
    private lateinit var navController: NavController
    private lateinit var navOptions: NavOptions

    private lateinit var viewType: ViewType

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

        viewType = intent.extras?.get("viewType") as ViewType

        startFragmentWithViewType()
    }

    private fun startFragmentWithViewType() {
        when(viewType) {
            ViewType.VIEW_SHARE_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionShareFragment(), navOptions)
            }
            ViewType.VIEW_PROFILE_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionProfileFragment(), navOptions)
            }
            ViewType.VIEW_LIST_CHAT_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionListChatFragment(), navOptions)
            }
            ViewType.VIEW_CHAT_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionChatFragment(), navOptions)
            }
            ViewType.VIEW_TARGET_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionTargetFragment(), navOptions)
            }
            ViewType.VIEW_REELS_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionReelsFragment(), navOptions)
            }
            ViewType.VIEW_FOLLOWS_FRAGMENT -> {
                navController.navigate(OthersNavDirections.actionFollowsFragment(), navOptions)
            }
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}