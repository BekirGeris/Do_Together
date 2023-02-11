package com.example.dotogether.view.adapter.holderListener

import com.example.dotogether.databinding.*
import com.example.dotogether.model.*
import com.example.dotogether.model.Target

class HolderListener {
    interface UserHolderListener{
        fun callback(binding: ItemUserBinding, user: User)
    }
    interface BigMemberHolderListener{
        fun callback(binding: ItemBigMemberBinding, user: User)
    }
    interface ChatHolderListener{
        fun callback(binding: ItemChatBinding, chat: Chat)
    }
    interface LeftMessageHolderListener{
        fun callback(binding: ItemMessageLeftBinding, message: Message)
    }
    interface MemberHolderListener{
        fun callback(binding: ItemMemberBinding, user: User)
    }
    interface RightMessageHolderListener{
        fun callback(binding: ItemMessageRightBinding, message: Message)
    }
    interface ProfileHolderListener{
        fun isMe(binding: ItemProfileBinding, user: User) : Boolean
        fun backgroundImageEdit(binding: ItemProfileBinding, user: User)
        fun profileImageEdit(binding: ItemProfileBinding, user: User)
        fun finishActivity(binding: ItemProfileBinding, user: User)
        fun logout(binding: ItemProfileBinding, user: User)
        fun deleteMyAccount(binding: ItemProfileBinding, user: User)
        fun follow(binding: ItemProfileBinding, user: User)
        fun unFollow(binding: ItemProfileBinding, user: User)
        fun showReels(binding: ItemProfileBinding, user: User)
        fun isOtherActivity() : Boolean {
            return false
        }
    }
    interface TargetHolderListener{
        fun like(binding: ItemTargetBinding, target: Target)
        fun join(binding: ItemTargetBinding, target: Target)
        fun unLike(binding: ItemTargetBinding, target: Target)
        fun unJoin(binding: ItemTargetBinding, target: Target)
        fun deleteTarget(binding: ItemTargetBinding, target: Target) {}
    }
    interface ReelsHolderListener{
        fun onClickReels(binding: ItemReelsBinding, user: User)
    }
    interface ReelsTopHolderListener{
        fun addReels()
    }
    interface NotificationHolderListener{
        fun onClickNotification(binding: ItemNotificationBinding, notification: Notification)
    }
}