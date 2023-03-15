package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.CreateTargetRequest
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(): BaseViewModel() {

    val period = MutableLiveData<String>().apply {
        value = "Daily"
    }

    private val _createTarget = MutableLiveData<Resource<Target>>()
    val createTarget: MutableLiveData<Resource<Target>> = _createTarget

    private val _target = MutableLiveData<Resource<Target>>()
    val target: MutableLiveData<Resource<Target>> = _target

    fun createTarget(createTargetRequest: CreateTargetRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.createTarget(createTargetRequest).collect {
                _createTarget.value = it
            }
        }
    }

    fun getTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getTarget(targetId).collect{
                _target.value = it
            }
        }
    }
}