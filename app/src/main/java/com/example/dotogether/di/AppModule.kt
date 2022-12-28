package com.example.dotogether.di

import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppRepository(
        localRepositoryImpl: LocalRepositoryImpl,
        remoteRepositoryImpl: RemoteRepositoryImpl
    ) = AppRepository(localRepositoryImpl, remoteRepositoryImpl)

}