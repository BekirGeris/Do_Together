package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.model.Page
import com.example.dotogether.model.Reels
import com.example.dotogether.model.User
import com.example.dotogether.model.request.CreateReelsRequest
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

    private val _reels = MutableLiveData<Resource<ArrayList<User>>>()
    val reels: MutableLiveData<Resource<ArrayList<User>>> = _reels

    private val _createReels = MutableLiveData<Resource<Reels>>()
    val createReels: MutableLiveData<Resource<Reels>> = _createReels

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

    fun createReels(createReelsRequest: CreateReelsRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.createReels(createReelsRequest).collect {
                _createReels.value = it
            }
        }
    }

    fun getFollowingsReels() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getFollowingsReels().collect {
                _reels.value = it
            }
        }
    }
}