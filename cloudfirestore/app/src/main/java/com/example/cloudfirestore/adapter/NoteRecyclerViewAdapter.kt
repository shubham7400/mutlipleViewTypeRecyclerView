package com.example.cloudfirestore.adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudfirestore.MainActivity
import com.example.cloudfirestore.NoteActivity
import com.example.cloudfirestore.R
import com.example.cloudfirestore.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class NoteRecyclerViewAdapter(private val notesList: MutableList<Note>, private val context: Context, private val firestoreDB: FirebaseFirestore) : RecyclerView.Adapter<NoteRecyclerViewAdapter.ViewHolder>() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_note, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]

        holder!!.title.text = note.title
        holder.content.text = note.content

        holder.edit.setOnClickListener {updateNote(note)}
        holder.delete.setOnClickListener { deleteNote(note.id!!, position) }


}

    override fun getItemCount(): Int {
        Log.i(TAG, "getItemCount:  size is "+notesList.size)
        return notesList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var content: TextView
        internal var edit: ImageView
        internal var delete: ImageView

        init {
            title = view.findViewById(R.id.tvTitle)
            content = view.findViewById(R.id.tvContent)

            edit = view.findViewById(R.id.ivEdit)
            delete = view.findViewById(R.id.ivDelete)
        }
    }

    private fun updateNote(note: Note) {
        auth = Firebase.auth

        var view = LayoutInflater.from(context).inflate(R.layout.alert_dialog,null)
        val etTitel = view.findViewById<EditText>(R.id.title)
        val etContent = view.findViewById<EditText>(R.id.content)
        etTitel.setText(note.title)
        etContent.setText(note.content)

        val id = note.id

        val dialogBuilder = AlertDialog.Builder(context).setView(view)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Edit", DialogInterface.OnClickListener {
                    dialog, position ->

                Toast.makeText(context,"Accept button got clicked",Toast.LENGTH_LONG).show()
                Log.d("tag","shubham mogarkar")

                val note = Note(id!!, auth.currentUser!!.uid, etTitel.text.toString(), etContent.text.toString()).toUpdateMap()

                firestoreDB!!.collection("notes").document(id).set(note).addOnSuccessListener {
                        Log.e(Companion.TAG, "Note document update successful!")
                        Toast.makeText(context, "Note has been updated!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e(Companion.TAG, "Error adding Note document", e)
                        Toast.makeText(context, "Note could not be updated!", Toast.LENGTH_SHORT).show()
                    }
                dialog.dismiss()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()

        // show alert dialog
        alert.show()

    }

    private fun deleteNote(id: String, position: Int) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setMessage("Do you really want to delete this note?")
            .setCancelable(false)
            .setPositiveButton("Confirm", DialogInterface.OnClickListener {
                    dialog, position ->

                firestoreDB.collection("notes")
                    .document(id)
                    .delete()
                    .addOnCompleteListener {
                        notesList.removeAt(position+1)
                        notifyItemRemoved(position+1)
                        notifyItemRangeChanged(position+1, notesList.size)
                        Toast.makeText(context, "Note has been deleted!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    }

                dialog.dismiss()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
        // create dialog box
        val alert = alertDialog.create()
        // show alert dialog
        alert.show()

    }

    companion object {
        private const val TAG = "NoteRecyclerViewAdapter"
    }


}