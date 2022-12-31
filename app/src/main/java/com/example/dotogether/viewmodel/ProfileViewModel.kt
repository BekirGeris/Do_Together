package com.example.dotogether.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Page
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : BaseViewModel() {

    private val _myTargets = MutableLiveData<Resource<Page<Target>>>()
    val myTargets: MutableLiveData<Resource<Page<Target>>> = _myTargets

    private val _nextMyTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextMyTargets: MutableLiveData<Resource<Page<Target>>> = _nextMyTargets

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

    fun getNextMyTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMyTargets(pageNo).collect {
                _nextMyTargets.value = it
            }
        }
    }
}