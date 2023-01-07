package com.example.dotogether.view.activity

import android.os.Bundle
import androidx.navigation.*
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.databinding.ActivityOthersBinding
import com.example.dotogether.util.Constants.ViewType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersActivity : BaseActivity() {

    private lateinit var binding: ActivityOthersBinding
    private lateinit var navController: NavController
    private lateinit var navOptions: NavOptions

    private var viewType: Int = 0

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

        viewType = intent.getIntExtra("viewType", 0)

        startFragmentWithViewType()
    }

    private fun startFragmentWithViewType() {
        when(viewType) {
            ViewType.VIEW_SHARE_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionShareFragment(), navOptions)
            }
            ViewType.VIEW_PROFILE_FRAGMENT.type -> {
                val userId = intent.getIntExtra("userId", -1)
                navController.navigate(OthersNavDirections.actionProfileFragment(userId), navOptions)
            }
            ViewType.VIEW_LIST_CHAT_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionListChatFragment(), navOptions)
            }
            ViewType.VIEW_CHAT_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionChatFragment(), navOptions)
            }
            ViewType.VIEW_TARGET_FRAGMENT.type -> {
                val targetId = intent.getIntExtra("targetId", -1)
                navController.navigate(OthersNavDirections.actionTargetFragment(targetId), navOptions)
            }
            ViewType.VIEW_FOLLOWS_FRAGMENT.type -> {
                val userId = intent.getIntExtra("userId", -1)
                val followsType = intent.getIntExtra("followsType", -1)
                navController.navigate(OthersNavDirections.actionFollowsFragment(userId, followsType), navOptions)
            }
            ViewType.VIEW_SEARCH_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionSearchFragment(), navOptions)
            }
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}