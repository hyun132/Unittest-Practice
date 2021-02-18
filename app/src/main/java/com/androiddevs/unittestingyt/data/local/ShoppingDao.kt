package com.androiddevs.unittestingyt.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    //livedata를 반환하기 때문에 suspend 붙일 필요 없음. livedata가 비동기이기 때문에 작동하지않거나 무시됨
    @Query("select * from shopping_items")
    fun observeAllShoppingItems():LiveData<List<ShoppingItem>>

    @Query("select sum(price*amount) from shopping_items")
    fun observeTotalPrice():LiveData<Float>
}