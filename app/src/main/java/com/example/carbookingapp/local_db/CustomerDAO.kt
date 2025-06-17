package com.example.carbookingapp.local_db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

//@Dao
//interface CustomerDAO {
//    @Insert
//    fun insert(customer: Customer)
//
//    @Update
//    fun update(customer: Customer) // based id, it will update the title and content
//
//    @Query("select * from customer")
//    fun getAll(): List<Customer>?
//
//    @Delete
//    fun delete(customer: Customer) // based on id, it will delete the data
//}
@Dao
interface CustomerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(customer: Customer)
}