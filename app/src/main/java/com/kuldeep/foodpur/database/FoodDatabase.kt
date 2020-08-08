package com.kuldeep.foodpur.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class,RestraEntity::class],version = 1)
abstract class FoodDatabase:RoomDatabase(){
    abstract fun foodDao():FoodDao
    abstract fun restraDao():RestraDao
}