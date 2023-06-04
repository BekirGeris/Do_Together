package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Notification
import com.example.dotogether.model.Page
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : BaseViewModel() {

    private val _notifications = MutableLiveData<Resource<Page<Notification>>>()
    val notifications: MutableLiveData<Resource<Page<Notification>>> = _notifications

    private val _nextNotifications = MutableLiveData<Resource<Page<Notification>>>()
    val nextNotifications: MutableLiveData<Resource<Page<Notification>>> = _nextNotifications

    fun getAllNotifications() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getAllNotifications().collect {
                _notifications.value = it
            }
        }
    }

    fun getNextAllNotifications(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextAllNotifications(pageNo).collect {
                _nextNotifications.value = it
            }
        }
    }

    fun notificationsReadAll() : MutableLiveData<Resource<Any>> {
        val notificationsReadAll = MutableLiveData<Resource<Any>>()
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.notificationsReadAll().collect {
                notificationsReadAll.value = it
            }
        }
        return notificationsReadAll
    }
}