package com.example.userdetail

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import java.util.*
import android.*
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class AddNewUserActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    lateinit var ivImage:ImageView
    lateinit var btCaptureImage:Button
    lateinit var btOpenGallery:Button

    lateinit var DOBPicBtn: Button
    lateinit var DOBTextView: TextView

    lateinit var usernameTextView:TextView
    lateinit var emailTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_user)

        DOBTextView = findViewById(R.id.dateOfBirthId)
        usernameTextView = findViewById(R.id.usernameTextViewId)
        emailTextView = findViewById(R.id.emailTextViewId)
        val button = findViewById<Button>(R.id.saveButtonId)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(usernameTextView.text) || TextUtils.isEmpty(emailTextView.text) || TextUtils.isEmpty(DOBTextView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val username = usernameTextView.text.toString()
                val email = emailTextView.text.toString()
                val dob = DOBTextView.text.toString()
                val userDetailArr = arrayListOf<String>(username,email,dob)
                replyIntent.putExtra(EXTRA_REPLY, userDetailArr)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }


/*calander  ------------------------------------------------------------------------------------------------code*/
        DOBPicBtn = findViewById(R.id.pickDateBtnId)
        DOBTextView = findViewById(R.id.dateOfBirthId)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        Log.d("tag","in button main activity")

        DOBPicBtn.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view: DatePicker, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    Log.d("tag", "in button")
                    DOBTextView.setText("" + dayOfMonth + "/ " + monthOfYear + "/ " + year)
                },
                year,
                month,
                day
            )
            dpd.show()
        }

/*calander  ------------------------------------------------------------------------------------------------code*/
        btCaptureImage = findViewById(R.id.btCapturePhotoId)
        ivImage = findViewById(R.id.ivImageId)
        btOpenGallery = findViewById(R.id.btOpenGallery)
        btCaptureImage.setOnClickListener {
            openCamera()
        }
        btOpenGallery.setOnClickListener {
            openGallery()
        }
    }
    override fun onResume() {
        super.onResume()
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION)
        }
    }
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                val bitmap = data?.extras?.get("data") as Bitmap
                ivImage.setImageBitmap(bitmap)
            }
            else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                ivImage.setImageURI(uri)
            }
        }
    }



    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

}