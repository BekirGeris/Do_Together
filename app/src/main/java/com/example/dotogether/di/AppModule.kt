package com.example.dotogether.di

import com.example.dotogether.BuildConfig
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.data.repostory.local.LocalRepository
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepository
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideLocalRepositoryImpl(localRepository: LocalRepository) = LocalRepositoryImpl(localRepository)

    @Singleton
    @Provides
    fun provideRemoteRepositoryImpl(remoteRepository: RemoteRepository) = RemoteRepositoryImpl(remoteRepository)

    @Singleton
    @Provides
    fun provideLocalRepository(): LocalRepository {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(LocalRepository::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteRepository(): RemoteRepository {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(RemoteRepository::class.java)
    }
}