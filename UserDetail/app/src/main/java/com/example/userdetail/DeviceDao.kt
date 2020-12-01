package com.example.userdetail


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DeviceDao {
    @Query("SELECT * FROM device_table where username = :username")
    fun getAllDevices(username:String): List<Device>

    @Insert
    fun insertDevice(device: Device)
}