package com.example.userdetail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class User (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "username")
    val username:String,
    @ColumnInfo(name = "username")
    val email:String,
    @ColumnInfo(name = "username")
    val date :String
)