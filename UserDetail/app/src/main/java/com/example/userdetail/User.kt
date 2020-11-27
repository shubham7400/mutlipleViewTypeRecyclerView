package com.example.userdetail

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
   /* @PrimaryKey(autoGenerate = true)
    val id:Int,*/
   val profilePhoto: Bitmap,




    var username:String,
   /* @ColumnInfo(name = "email")*/
    var email:String,
   /* @ColumnInfo(name = "date")*/
    var date :String


){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}