package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TargetViewModel @Inject constructor() : BaseViewModel() {

    private val _target = MutableLiveData<Resource<Target>>()
    val target: MutableLiveData<Resource<Target>> = _target

    private val _joinTarget = MutableLiveData<Resource<Target>>()
    val joinTarget: MutableLiveData<Resource<Target>> = _joinTarget

    fun getTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getTarget(targetId).collect{
                _target.value = it
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
}