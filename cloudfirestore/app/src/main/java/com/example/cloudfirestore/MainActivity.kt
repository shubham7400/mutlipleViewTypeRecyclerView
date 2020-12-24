package com.example.cloudfirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudfirestore.adapter.NoteRecyclerViewAdapter
import com.example.cloudfirestore.databinding.ActivityMainBinding
import com.example.cloudfirestore.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val TAG = "MainActivity"

    private var mAdapter: NoteRecyclerViewAdapter? = null

    private var firestoreDB: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.title = ""

        firestoreDB = FirebaseFirestore.getInstance()

        loadNotesList()

        binding.btAdd.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.profileImage.setOnClickListener {
            val intent =  Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        setProfile()
    }

    private fun setProfile() {
         firestoreDB!!.collection("Profile").get()
             .addOnCompleteListener { allProfile ->
                 if (allProfile.isSuccessful)
                 {
                      for (profile in allProfile.result!!)
                      {
                          if (profile["uid"] == auth.currentUser!!.uid)
                          {
                              binding.userName.text = profile["username"].toString()
                              Picasso.get().load(profile["profile"].toString()).into(binding.profileImage)
                          }
                      }
                 }
             }
             .addOnFailureListener{ error ->
                 Log.e(TAG, "Error setting profile", error)
                 Toast.makeText(applicationContext, "profile could not be set", Toast.LENGTH_SHORT).show()
             }
    }

    private fun loadNotesList() {
        Log.i(TAG, "loadNotesList: working list")
        firestoreDB!!.collection("notes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val notesList = mutableListOf<Note>()
                    Log.i(TAG, "loadNotesList:  running  success "+task.result)
                    for (doc in task.result!!) {
                        val note = doc.toObject<Note>(Note::class.java)
                        note.id = doc.id
                        Log.i(TAG, "loadNotesList: data   "+note.noteId+" not comming "+ auth.currentUser!!.uid)
                        if (note.noteId == auth.currentUser!!.uid)
                        {
                            Log.i(TAG, "loadNotesList: yess")
                            notesList.add(note)
                        }
                    }
                    Log.i(TAG, "loadNotesList: size "+notesList.size)
                    mAdapter = NoteRecyclerViewAdapter(notesList, this, firestoreDB!!)
                    binding.rvNoteList.layoutManager =  LinearLayoutManager(this)
                    binding.rvNoteList.itemAnimator = DefaultItemAnimator()
                    binding.rvNoteList.adapter = mAdapter
                } else {
                    Log.d(TAG, "Error getting documents: ", task.exception)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         if (item != null)
         {
             if (item.itemId == R.id.menu)
             {
                 FirebaseAuth.getInstance().signOut()
                 val intent = Intent(this, Welcome_Activity::class.java)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                 startActivity(intent)
                 finish()
             }
         }
        return super.onOptionsItemSelected(item)
    }

}