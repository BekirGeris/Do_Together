package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReelsViewModel @Inject constructor() : BaseViewModel() {

    private val _user = MutableLiveData<Resource<User>>()
    val user: MutableLiveData<Resource<User>> = _user

    private val _reels = MutableLiveData<Resource<ArrayList<User>>>()
    val reels: MutableLiveData<Resource<ArrayList<User>>> = _reels

    fun getUser(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getUser(userId).collect {
                _user.value = it
            }
        }
    }

    fun getFollowingsReels() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowingsReels().collect {
                _reels.value = it
            }
        }
    }
}