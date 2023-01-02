package com.example.dotogether.view.adapter.holder

import android.content.Intent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.view.activity.OthersActivity

abstract class BaseHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun goToProfileFragment(navController: NavController?, userId: Int) {
        navController.let {
            if (it != null) {
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(it.graph.startDestinationId, true)
                    .build()

                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionProfileFragment(userId), navOptions)
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_PROFILE_FRAGMENT.type)
                intent.putExtra("userId", userId)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToChatFragment(navController: NavController?) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionChatFragment())
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_CHAT_FRAGMENT.type)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToTargetFragment(navController: NavController?, targetId: Int) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionTargetFragment(targetId))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_TARGET_FRAGMENT.type)
                intent.putExtra("targetId", targetId)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToReelsFragment(navController: NavController?, userId: Int) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionReelsFragment(userId))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_REELS_FRAGMENT.type)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToFollowsFragment(navController: NavController?, userId: Int, followsType: Int) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionFollowsFragment(userId = userId, followsType = followsType))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_FOLLOWS_FRAGMENT.type)
                intent.putExtra("userId", userId)
                intent.putExtra("followsType", followsType)
                view.context.startActivity(intent)
            }
        }
    }
}