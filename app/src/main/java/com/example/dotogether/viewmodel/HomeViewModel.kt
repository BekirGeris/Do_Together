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

    private val _likeJoinLiveData = MutableLiveData<Resource<Target>>()
    val likeJoinLiveData: MutableLiveData<Resource<Target>> = _likeJoinLiveData

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

    fun joinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.joinTarget(targetId).collect{
                _likeJoinLiveData.value = it
            }
        }
    }

    fun likeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.likeTarget(targetId).collect{
                _likeJoinLiveData.value = it
            }
        }
    }

    fun unJoinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unJoinTarget(targetId).collect{
                _likeJoinLiveData.value = it
            }
        }
    }

    fun unLikeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unLikeTarget(targetId).collect{
                _likeJoinLiveData.value = it
            }
        }
    }
}