package com.example.dotogether.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var appRepository: AppRepository
}