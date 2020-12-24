package com.example.cloudfirestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudfirestore.databinding.ActivityNoteBinding
import com.example.cloudfirestore.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private lateinit var auth: FirebaseAuth

    private val TAG = "AddNoteActivity"

    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(binding.toolbarNote)
        supportActionBar!!.title = "Add"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbarNote.setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        auth = Firebase.auth
        firestoreDB = FirebaseFirestore.getInstance()

        binding.btAdd.setOnClickListener {
            Log.i(TAG, "onCreate: add button clicked")
            val title = binding.edtTitle.text.toString()
            val content = binding.edtContent.text.toString()

            addNote(auth.currentUser!!.uid, title, content)
        }
    }

    private fun addNote(noteId: String, title: String, content: String) {
        Log.i(TAG, "addNote: addNote comming")
        val note = Note(title, noteId, content).toMap()

        firestoreDB!!.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Note has been added!", Toast.LENGTH_SHORT).show()
               val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding Note document", e)
                Toast.makeText(applicationContext, "Note could not be added!", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}