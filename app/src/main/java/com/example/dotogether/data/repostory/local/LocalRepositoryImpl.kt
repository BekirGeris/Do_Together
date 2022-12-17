package com.example.dotogether.data.repostory.local

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocalRepositoryImpl @Inject constructor(private val repository: LocalRepository) : LocalRepository {

    override suspend fun localtest(key: String) {
        repository.localtest(key)
    }
}