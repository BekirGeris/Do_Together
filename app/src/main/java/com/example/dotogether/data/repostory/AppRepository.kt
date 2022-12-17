package com.example.dotogether.data.repostory

import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppRepository @Inject constructor(val localRepositoryImpl: LocalRepositoryImpl, val remoteRepositoryImpl: RemoteRepositoryImpl)