package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Page
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowsViewModel @Inject constructor() : BaseViewModel() {

    private val _users = MutableLiveData<Resource<Page<User>>>()
    val users: MutableLiveData<Resource<Page<User>>> = _users

    private val _nextUsers = MutableLiveData<Resource<Page<User>>>()
    val nextUsers: MutableLiveData<Resource<Page<User>>> = _nextUsers

    fun getFollowers(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowers(userId).collect {
                _users.value = it
            }
        }
    }

    fun getFollowings(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowings(userId).collect {
                _users.value = it
            }
        }
    }

    fun getNextFollowers(userId: Int, pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextFollowers(userId, pageNo).collect {
                _nextUsers.value = it
            }
        }
    }

    fun getNextFollowings(userId: Int, pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextFollowings(userId, pageNo).collect {
                _nextUsers.value = it
            }
        }
    }
}