package com.example.userdetail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Observer

class MainActivity : AppCompatActivity() {
    lateinit var fabInMain: FloatingActionButton
    private val newUserActivityRequestCode = 1

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UsersApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabInMain = findViewById<FloatingActionButton>(R.id.fabInMainId)
        Log.d("tag","check fab icon"+fabInMain)
        fabInMain.setOnClickListener{
            val intent = Intent(this, AddNewUserActivity::class.java)
            startActivityForResult(intent, newUserActivityRequestCode)

            /*val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewId)
            val adapter = UserListAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            userViewModel.allUsers.observe(this) { users ->
                // Update the cached copy of the words in the adapter.
                users.let { adapter.submitList(it) }
            }*/


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newUserActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(AddNewUserActivity.EXTRA_REPLY)?.let { reply ->
                val user = User(1,reply.get(0).toString(),reply.get(1).toString(),reply.get(2).toString())
                userViewModel.insert(user)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "data not saved",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}
