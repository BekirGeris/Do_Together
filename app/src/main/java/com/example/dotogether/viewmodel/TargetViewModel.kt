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

    private val _doneTarget = MutableLiveData<Resource<Target>>()
    val doneTarget: MutableLiveData<Resource<Target>> = _doneTarget

    fun doneTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.doneTarget(targetId).collect{
                _target.value = it
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