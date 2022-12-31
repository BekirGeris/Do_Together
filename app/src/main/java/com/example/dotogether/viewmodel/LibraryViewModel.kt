package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Page
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor() : BaseViewModel() {

    private val _joinedTargets = MutableLiveData<Resource<Page<Target>>>()
    val joinedTargets: MutableLiveData<Resource<Page<Target>>> = _joinedTargets

    private val _nextJoinedTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextJoinedTargets: MutableLiveData<Resource<Page<Target>>> = _nextJoinedTargets

    private val _likeTargets = MutableLiveData<Resource<Page<Target>>>()
    val likeTargets: MutableLiveData<Resource<Page<Target>>> = _likeTargets

    private val _nextLikeTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextLikeTargets: MutableLiveData<Resource<Page<Target>>> = _nextLikeTargets

    private val _doneTargets = MutableLiveData<Resource<Page<Target>>>()
    val doneTargets: MutableLiveData<Resource<Page<Target>>> = _doneTargets

    private val _nextDoneTargets = MutableLiveData<Resource<Page<Target>>>()
    val nextDoneTargets: MutableLiveData<Resource<Page<Target>>> = _nextDoneTargets

    fun getMyJoinedTargets() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyJoinedTargets().collect {
                _joinedTargets.value = it
            }
        }
    }

    fun getNextMyJoinedTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMyJoinedTargets(pageNo).collect {
                _nextJoinedTargets.value = it
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

    fun getNextMyLikeTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMyLikeTargets(pageNo).collect {
                _nextLikeTargets.value = it
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

    fun getNextMyDoneTargets(pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMyDoneTargets(pageNo).collect {
                _nextDoneTargets.value = it
            }
        }
    }

}