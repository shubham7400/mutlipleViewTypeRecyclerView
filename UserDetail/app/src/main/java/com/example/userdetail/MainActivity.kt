package com.example.userdetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Observer

class MainActivity : AppCompatActivity() {
    lateinit var fabInMain: FloatingActionButton
    private val newUserActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabInMain = findViewById<FloatingActionButton>(R.id.fabInMainId)
        Log.d("tag","check fab icon"+fabInMain)
        fabInMain.setOnClickListener{
            val intent = Intent(this, AddNewUserActivity::class.java)
            startActivityForResult(intent, newUserActivityRequestCode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK)
        {
            val reply = data?.getStringArrayExtra(AddNewUserActivity.EXTRA_REPLY)
            val user = User(reply?.get(0).toString(), reply?.get(1).toString(), reply?.get(2).toString())

         Log.d("tag ","${reply?.get(0)}  ${reply?.get(1)}  ${reply?.get(2)}")
        val db = UserRoomDatabase.getInstance(this)
            db.userDao().insertUser(user)
            Log.d("tag ", "data has beeb saved")
            val users = db.userDao().getAllUsers()
            Log.d("tag", "user list "+users)
            for (user in users)
            {
                    Log.d("tag", "user result "+user.username)
            }

        }else{
            Toast.makeText(

                    applicationContext,
                    "data not saved",
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}
