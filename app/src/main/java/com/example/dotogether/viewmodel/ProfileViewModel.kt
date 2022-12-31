package com.example.dotogether.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Page
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val _myUser = MutableLiveData<User>()
    val myUser: MutableLiveData<User> = _myUser

    private val _user = MutableLiveData<Resource<User>>()
    val user: MutableLiveData<Resource<User>> = _user

    private val _myTargets = MutableLiveData<Resource<Page<Target>>>()
    val myTargets: MutableLiveData<Resource<Page<Target>>> = _myTargets

    private val _nextMyTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextMyTargets: MutableLiveData<Resource<Page<Target>>> = _nextMyTargets

    fun logout() {
        viewModelScope.launch {
            appRepository.localRepositoryImpl.deleteAllUser()
        }
    }

    fun getMyUser() {
        viewModelScope.launch {
            val user = appRepository.localRepositoryImpl.getUser()
            user?.let {
                _myUser.value = it
            }
        }
    }

    fun getMyTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyTargets().collect {
                _myTargets.value = it
            }
        }
    }

    fun getNextMyTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMyTargets(pageNo).collect {
                _nextMyTargets.value = it
            }
        }
    }

    fun getUser(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getUser(userId).collect {
                _user.value = it
            }
        }
    }
}