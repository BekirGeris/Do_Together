package com.example.dotogether.view.adapter.holder

import android.content.Intent
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.util.Constants
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
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_PROFILE_FRAGMENT.type)
                intent.putExtra(Constants.USERID, userId)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToChatFragment(navController: NavController?, chatId: String?, chatUser: OtherUser, isGroup: Boolean) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionChatFragment(chatId = chatId, chatUser = chatUser, isGroup = isGroup))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_CHAT_FRAGMENT.type)
                intent.putExtra(Constants.CHAT_ID, chatId)
                intent.putExtra(Constants.CHAT_USER, chatUser)
                intent.putExtra(Constants.IS_GROUP, isGroup)
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
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_TARGET_FRAGMENT.type)
                intent.putExtra(Constants.TARGET_ID, targetId)
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
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_FOLLOWS_FRAGMENT.type)
                intent.putExtra(Constants.USERID, userId)
                intent.putExtra(Constants.FOLLOWS_TYPE, followsType)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToUserEditFragment(navController: NavController?, user: User) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionUserEditFragment(user = user))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_USER_EDIT_FRAGMENT.type)
                intent.putExtra(Constants.USER, user)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToPasswordEditFragment(navController: NavController?, user: User) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionPasswordEditFragment(user = user))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_PASSWORD_EDIT_FRAGMENT.type)
                intent.putExtra(Constants.USER, user)
                view.context.startActivity(intent)
            }
        }
    }

    fun goToReportFragment(navController: NavController?, targetId: Int) {
        navController.let {
            if (it != null) {
                with(it.graph.displayName.lowercase()) {
                    when {
                        contains("others_nav") -> {
                            it.navigate(OthersNavDirections.actionReportFragment(targetId))
                        }
                    }
                }
            } else {
                val intent = Intent(view.context, OthersActivity::class.java)
                intent.putExtra(Constants.VIEW_TYPE, ViewType.VIEW_REPORT_FRAGMENT.type)
                intent.putExtra(Constants.TARGET_ID, targetId)
                view.context.startActivity(intent)
            }
        }
    }
}