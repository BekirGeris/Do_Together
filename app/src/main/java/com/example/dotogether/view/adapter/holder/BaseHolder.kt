package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.HomeNavDirections
import com.example.dotogether.OthersNavDirections

abstract class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun goToProfileFragment(navController: NavController?) {
        navController?.let {
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setPopUpTo(navController.graph.startDestinationId, true)
                .build()

            with(navController.graph.displayName.lowercase()) {
                when {
                    contains("home_nav") -> {
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(viewType = 1))
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
                        navController.navigate(HomeNavDirections.actionGlobalOthersActivity(viewType = 3))
                    }
                    contains("others_nav") -> {
                        navController.navigate(OthersNavDirections.actionChatFragment())
                    }
                }
            }
        }
    }
}