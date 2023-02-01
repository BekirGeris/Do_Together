package com.example.dotogether.data.repostory.local

import androidx.lifecycle.LiveData
import com.example.dotogether.model.Basket
import com.example.dotogether.model.User

interface LocalRepository {

    suspend fun insertUser(user: User)

    suspend fun deleteAllUser()

    suspend fun getUser() : User?

    suspend fun insertBasket(basket: Basket)

    suspend fun updateBasket(basket: Basket)

    suspend fun nukeTableBasket()

    fun getCurrentBasket(): LiveData<Basket>

    fun getCurrentBasketSync(): Basket?
}