package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.view.callback.HolderCallback
import com.example.dotogether.view.fragment.ProfileFragmentDirections

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var onClickListener : HolderCallback

    fun setOnClickListener(onClickListener: HolderCallback) {
        this.onClickListener = onClickListener
    }

    fun getOnClickListener() : HolderCallback {
        return onClickListener
    }

    fun goToProfileFragment(navController: NavController?) {
        navController?.let {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.graph.startDestinationId, true)
                .build()

            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_PROFILE_FRAGMENT))
                    }
                    contains("others_nav") -> {
                        navController.navigate(OthersNavDirections.actionProfileFragment(), navOptions)
                    }
                }
            }
        }
    }

    fun goToChatFragment(navController: NavController?) {
        navController?.let {
            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_CHAT_FRAGMENT))
                    }
                    contains("others_nav") -> {
                        navController.navigate(OthersNavDirections.actionChatFragment())
                    }
                }
            }
        }
    }

    fun goToTargetFragment(navController: NavController?) {
        navController?.let {
            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_TARGET_FRAGMENT))
                    }
                    contains("others_nav") -> {
                        navController.navigate(OthersNavDirections.actionTargetFragment())
                    }
                }
            }
        }
    }

    fun goToReelsFragment(navController: NavController?) {
        navController?.let {
            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_REELS_FRAGMENT))
                    }
                    contains("others_nav") -> {
                        navController.navigate(OthersNavDirections.actionTargetFragment())
                    }
                }
            }
        }
    }

    fun goToFollowsFragment(navController: NavController?) {
        navController?.let {
            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(ViewType.VIEW_FOLLOWS_FRAGMENT))
                    }
                    contains("others_nav") -> {
                        navController.navigate(ProfileFragmentDirections.actionProfileFragmentToFollowsFragment())
                    }
                }
            }
        }
    }
}