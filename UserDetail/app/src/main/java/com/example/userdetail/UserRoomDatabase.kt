package com.example.userdetail

import android.content.Context

import androidx.room.Database

import androidx.room.Room

import androidx.room.RoomDatabase
import androidx.room.migration.Migration

import androidx.sqlite.db.SupportSQLiteDatabase

import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.launch



@Database(entities = arrayOf(User::class,Device::class), version = 3/*, exportSchema = false*/)

public abstract class UserRoomDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao

    companion object{

        private var INSTANCE: UserRoomDatabase? = null



        fun getInstance(context: Context):UserRoomDatabase{



            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(context.applicationContext, UserRoomDatabase::class.java, "word_database").allowMainThreadQueries().build()

                INSTANCE = instance

                // return instance

                instance

            }

        }

    }



   /* Room.databaseBuilder(applicationContext, MyDb::class.java, "database-name")
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()*/

}