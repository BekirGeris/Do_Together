package com.example.dotogether.data.repostory.remote

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RemoteRepositoryImpl @Inject constructor(private val repository: RemoteRepository) : RemoteRepository {

    override suspend fun remotetest(key: String) {
        repository.remotetest(key)
    }
}