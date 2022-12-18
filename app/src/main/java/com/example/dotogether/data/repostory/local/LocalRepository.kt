package com.example.dotogether.data.repostory.local

import com.example.dotogether.model.User

interface LocalRepository {

    suspend fun insertUser(user: User)

    suspend fun deleteAllUser()

    suspend fun getUser() : User?
}