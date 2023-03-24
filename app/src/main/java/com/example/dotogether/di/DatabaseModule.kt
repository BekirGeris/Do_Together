package com.example.dotogether.di

import android.content.Context
import androidx.room.Room
import com.example.dotogether.data.dao.BasketDao
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.data.db.AppDatabase
import com.example.dotogether.data.repostory.local.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideLocalRepositoryImpl(userDao: UserDao, basketDao: BasketDao) = LocalRepositoryImpl(userDao, basketDao)

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.userDao()

    @Singleton
    @Provides
    fun provideBasketDao(appDatabase: AppDatabase) = appDatabase.basketDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext app: Context) = Room.databaseBuilder(app, AppDatabase::class.java, "begers" )
        .allowMainThreadQueries()
        .addMigrations(*AppDatabase.getMigrations())
        .build()
}