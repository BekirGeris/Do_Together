package com.example.dotogether.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dotogether.data.dao.BasketDao
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.model.Basket
import com.example.dotogether.model.User

@Database(entities = [User::class, Basket::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun basketDao(): BasketDao
}