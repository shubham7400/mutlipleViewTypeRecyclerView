package com.example.alertdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var acceptButton: Button
    lateinit var cancelButton: Button
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
         var view = LayoutInflater.from(this).inflate(R.layout.alert_dialog,null)
        acceptButton = view.findViewById(R.id.acceptButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        textView = view.findViewById(R.id.textContent)

        AlertDialog.Builder(this).setView(view).create().show()

        acceptButton.setOnClickListener{
            Toast.makeText(this,"Accept button got clicked",Toast.LENGTH_LONG).show()
            Log.d("tag","shubham mogarkar")
        }
        cancelButton.setOnClickListener{
            Log.d("tag","shubham mogarkar")
            Toast.makeText(this,"cancel button got clicked",Toast.LENGTH_LONG).show()
        }

    }
}