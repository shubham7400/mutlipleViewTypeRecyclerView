package com.example.userdetail

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


class AddNewUserActivity : AppCompatActivity() {

    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private val usernamePattern = "[a-zA-Z][a-zA-Z0-9_]{7,29}\$"
    var isAllSet = false

    private val REQUEST_PERMISSION = 100

    private val REQUEST_IMAGE_CAPTURE = 1

    private val REQUEST_PICK_IMAGE = 2

    lateinit var ivImage:ImageView

    lateinit var btCaptureImage:Button

    lateinit var btOpenGallery:Button
 /*   lateinit var userImageView: ImageView*/
    lateinit var imageByteArray:ByteArray


    lateinit var DOBPicBtn: Button

    lateinit var DOBTextView: TextView



    lateinit var usernameTextView:TextView

    lateinit var emailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_new_user)

        setSupportActionBar(findViewById(R.id.toolbar_new_user))
        supportActionBar!!.title = "Add New User"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        findViewById<Toolbar>(R.id.toolbar_new_user).setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        DOBTextView = findViewById(R.id.dateOfBirthId)

        usernameTextView = findViewById(R.id.usernameTextViewId)

        emailTextView = findViewById(R.id.emailTextViewId)

        val button = findViewById<Button>(R.id.saveButtonId)
       /* userImageView = findViewById(R.id.ivImageId)*/
       /* val image = userImageView.drawable.toBitmap(10,10).toString()*/
    /*    val image = userImageView.*/
        button.setOnClickListener {

            val replyIntent = Intent()

            if (TextUtils.isEmpty(usernameTextView.text) || TextUtils.isEmpty(emailTextView.text) || (DOBTextView.text == "Date")) {
                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {

                val username = usernameTextView.text.toString()

                val email = emailTextView.text.toString()

                val dob = DOBTextView.text.toString()


                if (!(username.matches(usernamePattern.toRegex()))) {
                    usernameTextView.setError("Invalid username pattern! Please enter valid one")
                    usernameTextView.requestFocus()
                } else if (!(email.matches(emailPattern.toRegex()))) {
                    emailTextView.setError("Invalid email address pattern! Please valid one")
                    emailTextView.requestFocus()
                }else{
                    val userDetailArr = arrayOf<String>(username, email, dob)

                    Log.d("tag", "something " + userDetailArr.get(1))

                    replyIntent.putExtra(EXTRA_REPLY, userDetailArr)
                    replyIntent.putExtra("byteArray", imageByteArray)


                    setResult(Activity.RESULT_OK, replyIntent)
                    isAllSet = true
                }
                if(isAllSet)
                {
                    finish()
                    isAllSet = false
                }

            }

        }





/*calander  ------------------------------------------------------------------------------------------------code*/

        DOBPicBtn = findViewById(R.id.pickDateBtnId)

        DOBTextView = findViewById(R.id.dateOfBirthId)



        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)

        val month = c.get(Calendar.MONTH)

        val day = c.get(Calendar.DAY_OF_MONTH)

        Log.d("tag", "in button main activity")



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

            ActivityCompat.requestPermissions(
                    this,

                    arrayOf(Manifest.permission.CAMERA),

                    REQUEST_PERMISSION
            )

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
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                imageByteArray = stream.toByteArray()

            }

            else if (requestCode == REQUEST_PICK_IMAGE) {

                val uri = data?.getData()

                ivImage.setImageURI(uri)
               /* val iStream: InputStream? = contentResolver.openInputStream(uri!!)
              *//*  val inputData: ByteArray? = uri?.let { readBytes(this, it) }*/
               /* val iStream = contentResolver.openInputStream(uri!!)
                imageByteArray = getBytes(iStream!!)
*/
                val iStream = uri?.let { getContentResolver().openInputStream(it) }
                imageByteArray = iStream?.let { getBytes(it) }!!
            }

        }

    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    companion object {

        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"

    }



}