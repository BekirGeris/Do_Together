package com.example.dotogether.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import com.example.dotogether.model.Errors
import com.example.dotogether.model.Test
import com.example.dotogether.util.helper.RuntimeHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideGlide(@ApplicationContext context: Context) = RuntimeHelper.glide(context)
}