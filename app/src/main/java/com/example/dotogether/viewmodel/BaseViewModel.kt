package com.example.dotogether.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Basket
import com.example.dotogether.model.Tag
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.model.request.SearchRequest
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var appRepository: AppRepository

    private val _updateTarget = MutableLiveData<Resource<Target>>()
    val updateTarget: MutableLiveData<Resource<Target>> = _updateTarget

    private val _myUser = MutableLiveData<User>()
    val myUser: MutableLiveData<User> = _myUser

    private val _myUserRemote = MutableLiveData<Resource<User>>()
    val myUserRemote: MutableLiveData<Resource<User>> = _myUserRemote

    private val _tags = MutableLiveData<Resource<ArrayList<Tag>>>()
    val tags: MutableLiveData<Resource<ArrayList<Tag>>> = _tags

    protected val _updateUser = MutableLiveData<Resource<User>>()
    val updateUser: MutableLiveData<Resource<User>> = _updateUser

    fun joinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.joinTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun likeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.likeTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun unJoinTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unJoinTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun unLikeTarget(targetId: Int) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.unLikeTarget(targetId).collect{
                _updateTarget.value = it
            }
        }
    }

    fun getMyUserFromRemote() {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.getMyUserFromRemote().collect {
                _myUserRemote.value = it
            }
        }
    }

    fun searchTag(searchRequest: SearchRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.searchTag(searchRequest).collect {
                _tags.value = it
            }
        }
    }

    fun updateUser(updateUserRequest: UpdateUserRequest) {
        viewModelScope.launch {
            appRepository.remoteRepositoryImpl.updateUser(updateUserRequest).collect {
                _updateUser.value = it
            }
        }
    }

    //--------------------localRepositoryImpl--------------------------
    fun getMyUserFromLocale() {
        viewModelScope.launch {
            val user = appRepository.localRepositoryImpl.getUser()
            user?.let {
                _myUser.value = it
            }
        }
    }

    fun getCurrentBasket() : LiveData<Basket> {
        return appRepository.localRepositoryImpl.getCurrentBasket()
    }

    fun getCurrentBasketSync() : Basket? {
        return appRepository.localRepositoryImpl.getCurrentBasketSync()
    }

    fun insertBasket(basket: Basket) {
        viewModelScope.launch {
            appRepository.localRepositoryImpl.insertBasket(basket)
        }
    }

    fun updateBasket(basket: Basket) {
        viewModelScope.launch {
            appRepository.localRepositoryImpl.updateBasket(basket)
            Log.d(TAG, "updateBasket clear")
        }
    }
}