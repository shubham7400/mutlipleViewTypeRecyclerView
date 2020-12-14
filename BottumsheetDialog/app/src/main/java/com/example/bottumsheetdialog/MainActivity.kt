package com.example.bottumsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var shareLinearLayout: LinearLayout
    lateinit var copyLinearLayout: LinearLayout
    lateinit var uploadLinearLayout: LinearLayout
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var button: Button
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        textView = findViewById(R.id.textview)

        showCustomSheetDialog()
        button.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
         bottomSheetDialog.show()
    }

    private fun showCustomSheetDialog() {
         var view = LayoutInflater.from(this).inflate(R.layout.bottum_sheet,null)
        copyLinearLayout = view.findViewById(R.id.copyLinearLayout)
        shareLinearLayout = view.findViewById(R.id.shareLinearLayout)
        uploadLinearLayout = view.findViewById(R.id.uploadLinearLayout)

        copyLinearLayout.setOnClickListener(this)
        shareLinearLayout.setOnClickListener(this)
        uploadLinearLayout.setOnClickListener(this)

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(view)
    }



    override fun onClick(view: View?) {
         when(view)
         {
             shareLinearLayout ->{
                 Toast.makeText(this,"Share button got clicked", Toast.LENGTH_LONG).show()
                 textView.text = "shared"
                 bottomSheetDialog.dismiss()
             }

             uploadLinearLayout ->{
                 Toast.makeText(this,"Upload button got clicked", Toast.LENGTH_LONG)
                 textView.text = "updloaded"
                 bottomSheetDialog.dismiss()
             }

             copyLinearLayout ->{
                 Toast.makeText(this,"Copy button got clicked", Toast.LENGTH_LONG).show()
                 textView.text = "copied"
                 bottomSheetDialog.dismiss()
             }

         }
    }
}