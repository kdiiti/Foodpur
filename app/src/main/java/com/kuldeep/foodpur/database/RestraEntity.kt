package com.kuldeep.foodpur.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "restra")
data class RestraEntity(
    @PrimaryKey var id:Int,
    @ColumnInfo (name="restra_name") var name:String,
    @ColumnInfo(name="rating")    var rating:String,
    @ColumnInfo(name="cost_for_one") var cost_for_one:String,
    @ColumnInfo(name="image_url") var image_url:String


)