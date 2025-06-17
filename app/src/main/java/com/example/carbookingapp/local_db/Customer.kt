package com.example.carbookingapp.local_db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time
import java.util.Date

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val userID:Int=1,
    val pickAddress:String,
    val pickDate: String,
    val pickTime: String,
    val dropAddress:String,
    val dropDate:String,
    val dropTime:String
)
