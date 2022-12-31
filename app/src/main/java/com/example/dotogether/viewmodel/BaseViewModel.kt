package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var appRepository: AppRepository

    private val _likeJoinLiveData = MutableLiveData<Resource<Target>>()
    val likeJoinLiveData: MutableLiveData<Resource<Target>> = _likeJoinLiveData

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