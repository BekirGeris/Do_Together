package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var appRepository: AppRepository

    private val _updateTarget = MutableLiveData<Resource<Target>>()
    val updateTarget: MutableLiveData<Resource<Target>> = _updateTarget

    private val _myUser = MutableLiveData<User>()
    val myUser: MutableLiveData<User> = _myUser

    fun getMyUser() {
        viewModelScope.launch {
            val user = appRepository.localRepositoryImpl.getUser()
            user?.let {
                _myUser.value = it
            }
        }
    }

    fun joinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.joinTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun likeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.likeTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun unJoinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unJoinTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun unLikeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unLikeTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }
}