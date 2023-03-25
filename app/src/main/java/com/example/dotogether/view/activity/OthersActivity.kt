package com.example.dotogether.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.*
import com.example.dotogether.OthersNavDirections
import com.example.dotogether.R
import com.example.dotogether.databinding.ActivityOthersBinding
import com.example.dotogether.model.NotificationData
import com.example.dotogether.model.OtherUser
import com.example.dotogether.model.User
import com.example.dotogether.util.Constants.ViewType
import com.example.dotogether.util.helper.RuntimeHelper
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

        this.onBackPressedDispatcher.addCallback {
            if (!navController.popBackStack()) {
                this@OthersActivity.finish()
            }
        }
    }

    private fun initViews() {
        navController = Navigation.findNavController(this, R.id.othersFragmentContainerView)
        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestinationId, true)
            .build()

        viewType = intent.getIntExtra("viewType", 0)

        checkOpenAppFromNotification()
    }

    private fun checkOpenAppFromNotification() {
        val notificationData: NotificationData? = intent?.getParcelableExtra("notification_data")
        Log.d(RuntimeHelper.TAG, "bundle: $notificationData")

        val notificationType = notificationData?.type
        val typeId = notificationData?.typeId

        Log.d(RuntimeHelper.TAG, "notification type: $notificationType type id: $typeId")

        when {
            notificationType.equals("Notification", ignoreCase = true) -> {
                navController.navigate(OthersNavDirections.actionNotificationFragment(), navOptions)
            }
            notificationType.equals("Target", ignoreCase = true) && typeId?.toIntOrNull() != null -> {
                navController.navigate(OthersNavDirections.actionTargetFragment(typeId.toInt()), navOptions)
            }
            notificationType.equals("Chat", ignoreCase = true) && typeId?.toIntOrNull() != null-> {
                navController.navigate(OthersNavDirections.actionListChatFragment(chatId = typeId), navOptions)
            }
            else -> {
                startFragmentWithViewType()
            }
        }
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
                val chatId = intent.getStringExtra("chatId")
                navController.navigate(OthersNavDirections.actionListChatFragment(chatId = chatId), navOptions)
            }
            ViewType.VIEW_CHAT_FRAGMENT.type -> {
                val chatId = intent.getStringExtra("chatId")
                val chatUser = intent.getParcelableExtra<OtherUser>("chatUser")
                val isGroup = intent.getBooleanExtra("isGroup", false)
                navController.navigate(OthersNavDirections.actionChatFragment(chatId = chatId, chatUser = chatUser, isGroup = isGroup), navOptions)
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
            ViewType.VIEW_USER_EDIT_FRAGMENT.type -> {
                val user: User? = intent.getParcelableExtra("user")
                user?.let { navController.navigate(OthersNavDirections.actionUserEditFragment(user = it), navOptions) }
            }
            ViewType.VIEW_PASSWORD_EDIT_FRAGMENT.type -> {
                val user: User? = intent.getParcelableExtra("user")
                user?.let { navController.navigate(OthersNavDirections.actionPasswordEditFragment(user = it), navOptions) }
            }
            ViewType.VIEW_NOTIFICATION_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionNotificationFragment(), navOptions)
            }
            ViewType.VIEW_ADD_TAG_FRAGMENT.type -> {
                navController.navigate(OthersNavDirections.actionAddTagFragment(), navOptions)
            }
        }
    }
}