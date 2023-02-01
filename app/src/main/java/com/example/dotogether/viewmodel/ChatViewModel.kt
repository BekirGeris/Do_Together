package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.model.request.SendMessageRequest
import com.example.dotogether.model.request.NewChatRequest
import com.example.dotogether.model.response.MyChatsResponse
import com.example.dotogether.model.response.SendMessageResponse
import com.example.dotogether.model.response.NewChatResponse
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BaseViewModel() {

    private val _newChat = MutableLiveData<Resource<NewChatResponse>>()
    val newChat: MutableLiveData<Resource<NewChatResponse>> = _newChat

    private val _sendMessage = MutableLiveData<Resource<SendMessageResponse>>()
    val sendMessage: MutableLiveData<Resource<SendMessageResponse>> = _sendMessage

    private val _myChats = MutableLiveData<Resource<List<MyChatsResponse>>>()
    val myChats: MutableLiveData<Resource<List<MyChatsResponse>>> = _myChats

    private val _searchMyChats = MutableLiveData<Resource<List<MyChatsResponse>>>()
    val searchMyChats: MutableLiveData<Resource<List<MyChatsResponse>>> = _searchMyChats

    private val _chat = MutableLiveData<Resource<MyChatsResponse>>()
    val chat: MutableLiveData<Resource<MyChatsResponse>> = _chat

    private val _resetUnreadCountChat = MutableLiveData<Resource<MyChatsResponse>>()
    val resetUnreadCountChat: MutableLiveData<Resource<MyChatsResponse>> = _resetUnreadCountChat

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

    fun getChat(chatId: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getChat(chatId).collect{
                _chat.value = it
            }
        }
    }

    fun resetUnreadCountChat(chatId: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.resetUnreadCountChat(chatId).collect{
                _chat.value = it
            }
        }
    }
}