package com.example.dotogether.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dotogether.model.Basket

@Dao
interface BasketDao {

    @Insert
    suspend fun insert(basket: Basket)

    @Update
    suspend fun update(basket: Basket)

    @Query("DELETE FROM Basket")
    suspend fun nukeTable()

    @Query("select distinct * from basket order by localId desc Limit 1")
    fun getCurrentBasket(): LiveData<Basket>

    @Query("select distinct * from basket  order by localId desc Limit 1 ")
    fun getCurrentBasketSync(): Basket?
}