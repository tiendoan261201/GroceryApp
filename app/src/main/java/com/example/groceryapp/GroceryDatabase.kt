package com.example.groceryapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database
@Database(entities = [GroceryItems::class], version = 1)
//Ke thua roomdatabase
 abstract class GroceryDatabase : RoomDatabase(){

     abstract fun getGroceryDao():GroceryDAO

     companion object{
         @Volatile
         private var instance: GroceryDatabase? = null
         private val LOCK = Any()

         operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
             instance?: createDatabase(context).also {
                 instance = it
             }
         }
         private fun createDatabase(context: Context)=
             Room.databaseBuilder(context.applicationContext,
                 GroceryDatabase::class.java,
                 "Grocery.db"
             ).build()
     }

}