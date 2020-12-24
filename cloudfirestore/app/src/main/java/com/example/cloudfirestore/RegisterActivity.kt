package com.example.cloudfirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.cloudfirestore.databinding.ActivityNoteBinding
import com.example.cloudfirestore.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private var firestoreDB: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(findViewById(R.id.toolbar_Register))
        supportActionBar!!.title = "Register"

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.goToLoginTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        firestoreDB = FirebaseFirestore.getInstance()
        binding.registerBtn.setOnClickListener {
            val username = binding.usernameRegister.text.toString()
            val email = binding.emailRegister.text.toString()
            val password = binding.passwordRegister.text.toString()
            if (username.isEmpty() || username.isBlank() || username == "")
            {
                binding.usernameRegister.error = "username can not be blank"
                binding.usernameRegister.requestFocus()
            }else if (email.isBlank() || email.isEmpty() || email == "")
            {
                binding.emailRegister.error = "email can not be blank"
                binding.emailRegister.requestFocus()
            }else if (password.isEmpty() || password.isBlank() || password == "")
            {
                binding.passwordRegister.error = "password can not be blank"
                binding.passwordRegister.requestFocus()
            }else{
                createAccount(username,email,password)
            }

        }

    }



    private fun createAccount(username:String, email:String, password:String)
    {
        Log.i(TAG, "createAccount: create account")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(Companion.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val profileHashMap = HashMap<String,Any>()
                    profileHashMap["uid"] = auth.currentUser!!.uid
                    profileHashMap["username"] = username.toLowerCase()
                    profileHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/jsa-cloud-firestore-c1e62.appspot.com/o/profile.png?alt=media&token=7cb33f1c-d9ac-400f-b0db-148ab88aa245"
                    profileHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/jsa-cloud-firestore-c1e62.appspot.com/o/cover.png?alt=media&token=e4182745-79af-4b2b-912d-1a6a21b1318d"
                    profileHashMap["facebook"] = "https://m.facebook.com"
                    profileHashMap["instagram"] = "https://m.instagram.com"
                    profileHashMap["website"] = "https://www.google.com"
                    firestoreDB!!.collection("Profile").document(auth.currentUser!!.uid).set(profileHashMap)
                        .addOnSuccessListener {docRefId ->
                            Log.e(TAG, "DocumentSnapshot written with ID: " )
                            Toast.makeText(applicationContext, "profile has been added!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{  e ->
                            Log.e(TAG, "Error adding Note document", e)
                            Toast.makeText(applicationContext, "profile could not be added!", Toast.LENGTH_SHORT).show()
                        }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(Companion.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed. Please enter username, email and password in valid formate", Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    private fun updateUI(user: FirebaseUser?) {
            Log.i(TAG, "updateUI: user data "+ user!!.email+" and  "+ user!!.uid)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        finish()
    }


    companion object {
        private const val TAG = "RegisterActivity"
    }
}