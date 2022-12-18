package com.example.dotogether.data.repostory.local

import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.model.User
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocalRepositoryImpl @Inject constructor(private val userDao: UserDao) : LocalRepository {

    override suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    override suspend fun deleteAllUser() {
        userDao.deleteAll()
    }

    override suspend fun getUser() : User? {
        return userDao.getUser()
    }
}