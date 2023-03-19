package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.model.request.SendMessageRequest
import com.example.dotogether.model.request.NewChatRequest
import com.example.dotogether.model.response.ChatResponse
import com.example.dotogether.model.response.SendMessageResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel() {

    private val _newChat = MutableLiveData<Resource<ChatResponse>>()
    val newChat: MutableLiveData<Resource<ChatResponse>> = _newChat

    private val _sendMessage = MutableLiveData<Resource<SendMessageResponse>>()
    val sendMessage: MutableLiveData<Resource<SendMessageResponse>> = _sendMessage

    private val _myChats = MutableLiveData<Resource<List<ChatResponse>>>()
    val myChats: MutableLiveData<Resource<List<ChatResponse>>> = _myChats

    private val _searchMyChats = MutableLiveData<Resource<List<ChatResponse>>>()
    val searchMyChats: MutableLiveData<Resource<List<ChatResponse>>> = _searchMyChats

    private val _chat = MutableLiveData<Resource<ChatResponse>>()
    val chat: MutableLiveData<Resource<ChatResponse>> = _chat

    private val _updateChat = MutableLiveData<Resource<ChatResponse>>()
    val updateChat: MutableLiveData<Resource<ChatResponse>> = _updateChat

    private val _resetUnreadCountChat = MutableLiveData<Resource<ChatResponse>>()
    val resetUnreadCountChat: MutableLiveData<Resource<ChatResponse>> = _resetUnreadCountChat

    fun newChat(newChatRequest: NewChatRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.newChat(newChatRequest).collect{
                _newChat.value = it
            }
        }
    }

    fun sendMessage(sendMessageRequest: SendMessageRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.sendMessage(sendMessageRequest).collect{
                _sendMessage.value = it
            }
        }
    }

    fun myChats() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.myChats().collect{
                _myChats.value = it
            }
        }
    }

    fun searchMyChats(searchRequest: SearchRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.searchMyChats(searchRequest).collect{
                _searchMyChats.value = it
            }
        }
    }

    fun getChat(chatId: String) : MutableLiveData<Resource<ChatResponse>> {
        val chat = MutableLiveData<Resource<ChatResponse>>()
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getChat(chatId).collect{
                chat.value = it
                _chat.value = it
            }
        }
        return chat
    }

    fun muteChat(chatId: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.muteChat(chatId).collect{
                _updateChat.value = it
            }
        }
    }

    fun resetUnreadCountChat(chatId: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.resetUnreadCountChat(chatId)
        }
    }
}