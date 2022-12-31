package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.model.Page
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _allTargets = MutableLiveData<Resource<Page<Target>>>()
    val allTargets: MutableLiveData<Resource<Page<Target>>> = _allTargets

    private val _nextAllTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextAllTargets: MutableLiveData<Resource<Page<Target>>> = _nextAllTargets

    fun getAllTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getAllTargets().collect {
                _allTargets.value = it
            }
        }
    }

    fun getNextAllTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextAllTargets(pageNo).collect {
                _nextAllTargets.value = it
            }
        }
    }
}