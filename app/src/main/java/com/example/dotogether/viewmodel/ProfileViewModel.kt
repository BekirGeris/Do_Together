package com.example.dotogether.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val localRepositoryImpl: LocalRepositoryImpl) : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ProfileViewModel"
    }
    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            localRepositoryImpl.deleteAllUser()
        }
    }
}