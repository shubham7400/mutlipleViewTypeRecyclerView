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
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    lateinit var mAuth: FirebaseAuth
    lateinit var refUsers: DatabaseReference
    private var firebaseUserId:String = ""

    lateinit var email_login: EditText
    lateinit var password_login: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(findViewById(R.id.toolbar_login))
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        findViewById<Toolbar>(R.id.toolbar_login).setNavigationOnClickListener{
            val intent = Intent(this, Welcome_Activity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        var login_btn = findViewById<Button>(R.id.login_btn)
        email_login = findViewById(R.id.email_login)
        password_login = findViewById(R.id.password_login)
        login_btn.setOnClickListener{
            loginUser()
            val email = email_login.text.toString()
            val password = password_login.text.toString()
            if (email == ""){
                Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show()
            }else if (password == ""){
                Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show()
            }else{
                 mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->
                     if (task.isSuccessful){
                         val intent = Intent(this,MainActivity::class.java)
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                         startActivity(intent)
                         finish()
                     }else{
                         Toast.makeText(this,"Error messages : "+ task.exception?.message.toString(),Toast.LENGTH_LONG).show()
                     }
                 }
            }
        }

    }

    private fun loginUser() {

    }
}