package com.example.userdetail

import androidx.lifecycle.LiveData

import androidx.room.Dao

import androidx.room.Insert

import androidx.room.Query

import java.util.concurrent.Flow



@Dao

interface UserDao {

    @Query("SELECT * FROM user_table")

    fun getAllUsers(): List<User>

    @Query("select username from user_table where username = :username")
    fun isUserExist(username:String): String

    @Insert

    fun insertUser(user:User)

}