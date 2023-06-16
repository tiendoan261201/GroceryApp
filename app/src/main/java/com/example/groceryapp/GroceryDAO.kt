package com.example.groceryapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//DAO insert delete, ua update dau nhi ???
@Dao
interface GroceryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroceryItems)

    @Delete
    suspend fun delete(item:GroceryItems)

    @Query("SELECT * FROM grocerry_items")
    fun getAllGroceryItems() : LiveData<List<GroceryItems>>
}