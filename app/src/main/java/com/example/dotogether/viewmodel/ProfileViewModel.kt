package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val _myTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val myTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _myTargets

    fun logout() {
        viewModelScope.launch {
            appRepository.localRepositoryImpl.deleteAllUser()
        }
    }

    fun getMyTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyTargets().collect {
                _myTargets.value = it
            }
        }
    }
}