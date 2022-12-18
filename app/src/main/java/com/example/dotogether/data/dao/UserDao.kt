package com.example.dotogether.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dotogether.model.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("delete from user")
    fun deleteAll()

    @Query("select * from user")
    fun getUser() : User?
}