package com.example.cloudfirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.cloudfirestore.databinding.ActivityLoginBinding
import com.example.cloudfirestore.databinding.ActivityNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(findViewById(R.id.toolbar_login))
        supportActionBar!!.title = "Login"

        auth = Firebase.auth

        binding.goToRegisterTV.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            if (email.isBlank() || email.isEmpty() || email == "")
            {
                binding.emailLogin.error = "email can not be blank"
                binding.emailLogin.requestFocus()
            }else if (password.isEmpty() || password.isBlank() || password == "")
            {
                binding.passwordLogin.error = "password can not be blank"
                binding.passwordLogin.requestFocus()
            }else{
                login(email,password)
            }
        }
    }

    private fun login(email:String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Companion.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Companion.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.  Please try with valid email and password", Toast.LENGTH_LONG).show()

                    // ...
                }

                // ...
            }
    }

    private fun updateUI(user: FirebaseUser?) {
            val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }

}