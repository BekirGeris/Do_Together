package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.request.SendMessageForTargetRequest
import com.example.dotogether.model.request.SendMessageForUserRequest
import com.example.dotogether.model.response.SendMessageForTargetResponse
import com.example.dotogether.model.response.SendMessageForUserResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel() {

    private val _sendMessageForUser = MutableLiveData<Resource<SendMessageForUserResponse>>()
    val sendMessageForUser: MutableLiveData<Resource<SendMessageForUserResponse>> = _sendMessageForUser

    private val _sendMessageForTarget = MutableLiveData<Resource<SendMessageForTargetResponse>>()
    val sendMessageForTarget: MutableLiveData<Resource<SendMessageForTargetResponse>> = _sendMessageForTarget

    fun sendMessageForUser(sendMessageForUserRequest: SendMessageForUserRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.sendMessageForUser(sendMessageForUserRequest).collect{
                _sendMessageForUser.value = it
            }
        }
    }

    fun sendMessageForTarget(sendMessageForTargetRequest: SendMessageForTargetRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.sendMessageForTarget(sendMessageForTargetRequest).collect{
                _sendMessageForTarget.value = it
            }
        }
    }
}