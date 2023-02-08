package com.example.dotogether.data.repostory.local

import androidx.lifecycle.LiveData
import com.example.dotogether.data.dao.BasketDao
import com.example.dotogether.data.dao.UserDao
import com.example.dotogether.model.Basket
import com.example.dotogether.model.User
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class LocalRepositoryImpl @Inject constructor(private val userDao: UserDao, private val basketDao: BasketDao) : LocalRepository {

    override suspend fun insertUser(user: User) {
        userDao.deleteAll()
        userDao.insert(user)
    }

    override suspend fun deleteAllUser() {
        userDao.deleteAll()
    }

    override suspend fun getUser() : User? {
        return userDao.getUser()
    }

    override suspend fun insertBasket(basket: Basket) {
        basketDao.nukeTable()
        basketDao.insert(basket)
    }

    override suspend fun updateBasket(basket: Basket) {
        if (getCurrentBasketSync() != null) {
            basketDao.update(basket)
        } else {
            insertBasket(basket)
        }
    }

    override suspend fun nukeTableBasket() {
        basketDao.nukeTable()
    }

    override fun getCurrentBasket(): LiveData<Basket> {
        return basketDao.getCurrentBasket()
    }

    override fun getCurrentBasketSync(): Basket? {
        return basketDao.getCurrentBasketSync()
    }
}