package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _allTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val allTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _allTargets

    fun getAllTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getAllTargets().collect {
                _allTargets.value = it
            }
        }
    }
}