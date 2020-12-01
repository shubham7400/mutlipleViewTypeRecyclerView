package com.example.userdetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserDetailActivity : AppCompatActivity() {
    lateinit var fabSaveDetail: FloatingActionButton
    private val addUserDetailActivityRequestCode = 2
    lateinit var userRoomDatabase: UserRoomDatabase
    lateinit var username:String
    lateinit var allDevices: List<Device>
    lateinit var recyclerView: RecyclerView
    lateinit var usernameTextView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        setSupportActionBar(findViewById(R.id.toolbar_device_list))
        supportActionBar!!.title = "Devices"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        findViewById<Toolbar>(R.id.toolbar_device_list).setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        username = intent.getStringExtra("username").toString()
        usernameTextView = findViewById(R.id.username)
        usernameTextView.text = "Username : "+username

        fabSaveDetail = findViewById(R.id.fabSaveDetailId)

        fabSaveDetail.setOnClickListener{

            val intent = Intent(this, AddUserDetailActivity::class.java)

            startActivityForResult(intent, addUserDetailActivityRequestCode)

        }

        recyclerView = findViewById(R.id.deviceListRecyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)


        userRoomDatabase = UserRoomDatabase.getInstance(this)
        allDevices = userRoomDatabase.deviceDao().getAllDevices(username)
        recyclerView.adapter = DeviceAdapter(allDevices)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == addUserDetailActivityRequestCode && resultCode == Activity.RESULT_OK)
        {
            val reply = data?.getStringArrayExtra(AddNewUserActivity.EXTRA_REPLY)
            val deviceImageByteArray = data?.getByteArrayExtra("deviceImageByteArray")
            val device = deviceImageByteArray?.let { Device(username,reply?.get(0).toString(), reply?.get(1).toString(), reply?.get(2).toString(), it) }
            Log.d("tag ","${reply?.get(0)}  ${reply?.get(1)}  ${reply?.get(2)}")
            /* userRoomDatabase = UserRoomDatabase.getInstance(this)*/
            if (device != null) {
                userRoomDatabase.deviceDao().insertDevice(device)
            }
            Log.d("tag ", "data has beeb saved")
            allDevices = userRoomDatabase.deviceDao().getAllDevices(username)
            Log.d("tag", "user list "+allDevices)
            for (device in allDevices)
            {
                Log.d("tag", "user result "+device.deviceName)
            }
            recyclerView.adapter = DeviceAdapter(allDevices)
        }else{
            Toast.makeText(applicationContext, "data not saved", Toast.LENGTH_LONG).show()
        }
    }


}