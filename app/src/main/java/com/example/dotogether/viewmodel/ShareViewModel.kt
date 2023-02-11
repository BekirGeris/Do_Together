package com.example.dotogether.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Tag
import com.example.dotogether.model.Target
import com.example.dotogether.model.request.CreateTargetRequest
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(): BaseViewModel() {

    val period = MutableLiveData<String>().apply {
        value = "Daily"
    }

    private val _createTarget = MutableLiveData<Resource<Target>>()
    val createTarget: MutableLiveData<Resource<Target>> = _createTarget

    fun createTarget(createTargetRequest: CreateTargetRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.createTarget(createTargetRequest).collect {
                _createTarget.value = it
            }
        }
    }
}