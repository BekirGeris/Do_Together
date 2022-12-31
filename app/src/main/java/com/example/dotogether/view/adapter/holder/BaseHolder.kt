package com.example.dotogether.view.adapter.holder

import android.content.Intent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.view.activity.OthersActivity
import com.example.dotogether.view.callback.HolderCallback

abstract class BaseHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private lateinit var onClickListener : HolderCallback

    fun setOnClickListener(onClickListener: HolderCallback) {
        this.onClickListener = onClickListener
    }

    fun getOnClickListener() : HolderCallback {
        return onClickListener
    }

    fun goToProfileFragment(navController: NavController?) {
        navController.let {
            if (it != null) {
                val navOptions = NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(it.graph.startDestinationId, true)
                    .build()

                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionProfileFragment(), navOptions)
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_PROFILE_FRAGMENT.type)
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

    fun goToReelsFragment(navController: NavController?) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionReelsFragment())
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

    fun goToFollowsFragment(navController: NavController?) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionFollowsFragment())
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra("viewType", ViewType.VIEW_FOLLOWS_FRAGMENT.type)
                view.context.startActivity(intent)
            }
        }
    }
}