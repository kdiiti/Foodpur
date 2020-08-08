package com.kuldeep.foodpur.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestraDao {
    @Insert
    fun insertResta(restraEntity: RestraEntity)

    @Delete
    fun deleteRestra(restraEntity: RestraEntity)

    @Query("SELECT * FROM restra ")
    fun getAllRestra():List<RestraEntity>

    @Query("SELECT * FROM restra WHERE id=:restraId")
    fun getRestraById(restraId:Int):RestraEntity
}