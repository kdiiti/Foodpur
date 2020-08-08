package com.kuldeep.foodpur.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {
    @Insert
    fun insertOrder(foodEntity: FoodEntity)

    @Delete
    fun deleteOrder(foodEntity: FoodEntity)

    @Query("SELECT * FROM orders")
    fun getAllOrders():List<FoodEntity>

    @Query("DELETE FROM orders WHERE resId = :resId")
    fun deleteOrders(resId: String)

}