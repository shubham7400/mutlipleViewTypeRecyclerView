package com.example.messangerapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    lateinit var mAuth: FirebaseAuth
    lateinit var refUsers: DatabaseReference

    private var firebaseUserId:String = ""

    lateinit var username_register: EditText
    lateinit var email_register: EditText
    lateinit var password_register: EditText
    lateinit var register_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(findViewById(R.id.toolbar_Register))
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        findViewById<Toolbar>(R.id.toolbar_Register).setNavigationOnClickListener{
            val intent = Intent(this, Welcome_Activity::class.java)
            startActivity(intent)
            finish()
        }

        username_register = findViewById<EditText>(R.id.username_register)
        email_register = findViewById<EditText>(R.id.email_register)
        password_register = findViewById<EditText>(R.id.password_register)
        register_btn = findViewById<Button>(R.id.register_btn)
        register_btn.setOnClickListener{
            register()
        }
    }

    private fun register() {
         mAuth = FirebaseAuth.getInstance()

        val username = username_register.text.toString()
        val email = email_register.text.toString()
        val password = password_register.text.toString()
        if (username == ""){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_LONG).show()
        }else if (email == ""){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show()
        }else if(password == ""){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Register succesfully",Toast.LENGTH_LONG).show()
                    firebaseUserId = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)
                    val userHashMap = HashMap<String,Any>()
                    userHashMap["uid"] = firebaseUserId
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/messangerapp-2bbac.appspot.com/o/ic_profile.png?alt=media&token=8e701796-4fad-4046-a194-5f3f46922f19"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/messangerapp-2bbac.appspot.com/o/cover.jpg?alt=media&token=2a636b4b-574b-4535-812f-96304165d9d5"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap).addOnCompleteListener{task ->

                    }

                }else{
                    Toast.makeText(this,"Error messages : "+ task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}