package com.kuldeep.foodpur.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "orders")
data class FoodEntity (
    @PrimaryKey val resId:String,
    @ColumnInfo(name="food_itmes") val foodItems:String
    )