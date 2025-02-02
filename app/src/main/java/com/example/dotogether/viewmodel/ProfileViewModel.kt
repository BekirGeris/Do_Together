package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Page
import com.example.dotogether.model.Reels
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.UpdatePasswordRequest
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val _user = MutableLiveData<Resource<User>>()
    val user: MutableLiveData<Resource<User>> = _user

    private val _targets = MutableLiveData<Resource<Page<Target>>>()
    val targets: MutableLiveData<Resource<Page<Target>>> = _targets

    private val _nextTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextTargets: MutableLiveData<Resource<Page<Target>>> = _nextTargets

    private val _deleteTarget = MutableLiveData<Resource<Target>>()
    val deleteTarget: MutableLiveData<Resource<Target>> = _deleteTarget

    private val _removeReels = MutableLiveData<Resource<Reels>>()
    val removeReels: MutableLiveData<Resource<Reels>> = _removeReels

    private val _updatePassword = MutableLiveData<Resource<User>>()
    val updatePassword: MutableLiveData<Resource<User>> = _updatePassword

    fun logout() {
        viewModelScope.launch {
            appRepository.localRepositoryImpl.deleteAllUser()
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

    fun deleteTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.deleteTarget(targetId).collect{
                _deleteTarget.value = it
            }
        }
    }

    fun removeReels(reelsId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.removeReels(reelsId).collect{
                _removeReels.value = it
            }
        }
    }

    fun updatePassword(updatePasswordRequest: UpdatePasswordRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.updatePassword(updatePasswordRequest).collect{
                _updatePassword.value = it
                if (it is Resource.Success) {
                    it.data?.password = updatePasswordRequest.new_password
                    it.data?.let { user -> appRepository.localRepositoryImpl.insertUser(user) }
                }
            }
        }
    }

    fun deleteMyAccount() : MutableLiveData<Resource<User>> {
        val result = MutableLiveData<Resource<User>>()
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.deleteMyAccount().collect{
                result.value = it
            }
        }
        return result
    }

    fun clearMyFirebaseToken() {
        val updateUserRequest = UpdateUserRequest(fcm_token = "")
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.updateUser(updateUserRequest).collect()
        }
    }
}