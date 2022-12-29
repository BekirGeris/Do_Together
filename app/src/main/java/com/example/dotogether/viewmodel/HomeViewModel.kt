package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _allTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val allTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _allTargets

    private val _joinTarget = MutableLiveData<Resource<Target>>()
    val joinTarget: MutableLiveData<Resource<Target>> = _joinTarget

    private val _likeTarget = MutableLiveData<Resource<Target>>()
    val likeTarget: MutableLiveData<Resource<Target>> = _likeTarget

    fun getAllTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getAllTargets().collect {
                _allTargets.value = it
            }
        }
    }

    fun joinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.joinTarget(targetId).collect{
                _joinTarget.value = it
            }
        }
    }

    fun likeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.likeTarget(targetId).collect{
                _likeTarget.value = it
            }
        }
    }
}