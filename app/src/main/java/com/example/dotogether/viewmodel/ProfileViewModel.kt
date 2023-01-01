package com.example.dotogether.viewmodel

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

    private val _targets = MutableLiveData<Resource<Page<Target>>>()
    val targets: MutableLiveData<Resource<Page<Target>>> = _targets

    private val _nextTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextTargets: MutableLiveData<Resource<Page<Target>>> = _nextTargets

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

    fun getTargetsWithUserId(userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getTargetsWithUserId(userId).collect {
                _targets.value = it
            }
        }
    }

    fun getNextTargetsWithUserId(userId: Int, pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextTargetsWithUserId(userId, pageNo).collect {
                _nextTargets.value = it
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