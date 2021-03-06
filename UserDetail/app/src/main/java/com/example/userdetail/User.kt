package com.example.userdetail


import android.graphics.Bitmap

import androidx.room.ColumnInfo


import androidx.room.Entity

import androidx.room.PrimaryKey


@Entity(tableName = "user_table")

data class User (
    /* @PrimaryKey(autoGenerate = true)
     val id:Int,*/
    /*val profilePhoto: Bitmap,*/
    @PrimaryKey
    var username:String,
    /* @ColumnInfo(name = "email")*/
    var email:String,
    /* @ColumnInfo(name = "date")*/
    var date :String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    /*private byte[] image; */
    var image: ByteArray
   /* var profilePhoto: Bitmap*/
)/*{

    @PrimaryKey(autoGenerate = true)

    var id:Int = 0

}*/