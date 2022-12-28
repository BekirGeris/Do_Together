package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.response.GetAllTargetsResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : BaseViewModel() {

    private val _joinedTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val joinedTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _joinedTargets

    private val _likeTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val likeTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _likeTargets

    private val _doneTargets = MutableLiveData<Resource<GetAllTargetsResponse>>()
    val doneTargets: MutableLiveData<Resource<GetAllTargetsResponse>> = _doneTargets

    fun getJoinedTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyJoinedTargets().collect {
                _joinedTargets.value = it
            }
        }
    }

    fun getMyLikeTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyLikeTargets().collect {
                _likeTargets.value = it
            }
        }
    }

    fun getMyDoneTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyDoneTargets().collect {
                _doneTargets.value = it
            }
        }
    }

}