package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Action
import com.example.dotogether.model.Page
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TargetViewModel @Inject constructor() : BaseViewModel() {

    private val _target = MutableLiveData<Resource<Target>>()
    val target: MutableLiveData<Resource<Target>> = _target

    private val _members = MutableLiveData<Resource<Page<User>>>()
    val members: MutableLiveData<Resource<Page<User>>> = _members

    private val _nextMembers = MutableLiveData<Resource<Page<User>>>()
    val nextMembers: MutableLiveData<Resource<Page<User>>> = _nextMembers

    private val _searchMembers = MutableLiveData<Resource<ArrayList<User>>>()
    val searchMembers: MutableLiveData<Resource<ArrayList<User>>> = _searchMembers

    private val _removeUser = MutableLiveData<Resource<User>>()
    val removeUser: MutableLiveData<Resource<User>> = _removeUser

    fun doneTarget(targetId: Int): MutableLiveData<Resource<Target>> {
        val doneTarget = MutableLiveData<Resource<Target>>()
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.doneTarget(targetId).collect{
                doneTarget.value = it
            }
        }
        return doneTarget
    }

    fun getTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getTarget(targetId).collect{
                _target.value = it
            }
        }
    }

    fun getActions(targetId: Int) : MutableLiveData<Resource<ArrayList<Action>>>{
        val actions = MutableLiveData<Resource<ArrayList<Action>>>()
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getActions(targetId).collect {
                actions.value = it
            }
        }
        return actions
    }

    fun getMembers(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMembers(targetId).collect {
                _members.value = it
            }
        }
    }

    fun getNextMembers(targetId: Int, pageNo: String) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getNextMembers(targetId, pageNo).collect {
                _nextMembers.value = it
            }
        }
    }

    fun searchMembers(searchRequest: SearchRequest, targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.searchMembers(searchRequest, targetId).collect {
                _searchMembers.value = it
            }
        }
    }

    fun removeUserFromTarget(targetId: Int, userId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.removeUserFromTarget(targetId, userId).collect{
                _removeUser.value = it
            }
        }
    }
}