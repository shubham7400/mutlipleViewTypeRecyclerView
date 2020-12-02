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
                    userHashMap["username"] = username.toLowerCase()
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/messangerapp-2bbac.appspot.com/o/profile.png?alt=media&token=6fb05330-12f5-4253-8f03-1f20b3f30c0d"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/messangerapp-2bbac.appspot.com/o/cover.png?alt=media&token=b56c3f75-f8b7-4866-92b4-e0617c2ad127"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = username.toLowerCase()
                    userHashMap["facebook"] = "https://m.facebook.com"
                    userHashMap["instagram"] = "https://m.instagram.com"
                    userHashMap["website"] = "https://www.google.com"

                    refUsers.updateChildren(userHashMap).addOnCompleteListener{task ->
                        if(task.isSuccessful)
                        {
                            val intent = Intent(this,MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                }else{
                    Toast.makeText(this,"Error messages : "+ task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}