package com.example.carbookingapp.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Customer::class], version = 1)
abstract class CustomerDatabase : RoomDatabase() {
    // create abstract function which will return dao
    abstract fun customerDAO(): CustomerDAO

    // create the CustomerDatabase object
    companion object {
        private var INSTANCE: CustomerDatabase? = null
        fun getInstance(context: Context): CustomerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    CustomerDatabase::class.java,
                    "customer_database"
                ).build() // create database if not exists, create tables if not exists, dao object
                INSTANCE = instance
                instance
            }
        }
    }
}
