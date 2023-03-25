package com.example.dotogether.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.model.Discover
import com.example.dotogether.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : BaseViewModel() {

    private val _allDiscover = MutableLiveData<Resource<ArrayList<Discover>>>()
    val allDiscover: MutableLiveData<Resource<ArrayList<Discover>>> = _allDiscover

    fun getAllDiscover() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getAllDiscover().collect {
                _allDiscover.value = it
            }
        }
    }
}