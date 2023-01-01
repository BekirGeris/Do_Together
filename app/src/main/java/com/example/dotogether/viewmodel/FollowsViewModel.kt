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

    private val _user = MutableLiveData<Resource<Page<User>>>()
    val user: MutableLiveData<Resource<Page<User>>> = _user

    fun getFollowers(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowers(userId).collect {
                _user.value = it
            }
        }
    }

    fun getFollowings(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowings(userId).collect {
                _user.value = it
            }
        }
    }
}