package com.example.dotogether.di

import android.content.Context
import androidx.room.Room
import com.example.dotogether.BuildConfig
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.data.db.AppDatabase
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.data.repostory.local.LocalRepository
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import com.example.dotogether.data.repostory.remote.RemoteRepository
import com.example.dotogether.data.repostory.remote.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideLocalRepositoryImpl(userDao: UserDao) = LocalRepositoryImpl(userDao)

    @Singleton
    @Provides
    fun provideRemoteRepositoryImpl(remoteRepository: RemoteRepository) = RemoteRepositoryImpl(remoteRepository)

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideLocalRepository(retrofit: Retrofit) = retrofit.create(LocalRepository::class.java)

    @Singleton
    @Provides
    fun provideRemoteRepository(retrofit: Retrofit) = retrofit.create(RemoteRepository::class.java)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(app, AppDatabase::class.java, "your_db_name" )
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()
}