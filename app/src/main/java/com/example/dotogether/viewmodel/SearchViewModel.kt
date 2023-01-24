package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : BaseViewModel() {

    private val _users = MutableLiveData<Resource<ArrayList<User>>>()
    val users: MutableLiveData<Resource<ArrayList<User>>> = _users

    private val _targets = MutableLiveData<Resource<ArrayList<Target>>>()
    val targets: MutableLiveData<Resource<ArrayList<Target>>> = _targets

    fun searchUser(searchRequest: SearchRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.searchUser(searchRequest).collect {
                _users.value = it
            }
        }
    }

    fun searchTarget(searchRequest: SearchRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.searchTarget(searchRequest).collect {
                _targets.value = it
            }
        }
    }
}