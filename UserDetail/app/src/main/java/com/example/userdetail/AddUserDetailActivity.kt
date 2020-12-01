package com.example.userdetail


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class AddUserDetailActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2
    lateinit var ivDeviceImage: ImageView
    lateinit var btDeviceCaptureImage: Button
    lateinit var btOpenGallery: Button
    lateinit var DatePicBtn: Button
    lateinit var deviceDateTextView: TextView

    lateinit var deviceNameTextView: TextView
    lateinit var deviceModelTextView: TextView
    lateinit var deviceImageByteArray: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_detail)

        deviceDateTextView = findViewById(R.id.deviceAddDateTextViewId)
        deviceNameTextView = findViewById(R.id.deviceNameEditTextId)
        deviceModelTextView = findViewById(R.id.deviceModelEditTextId)
        val saveDetailButton = findViewById<Button>(R.id.saveDetailButtonId)

        saveDetailButton.setOnClickListener {

            val replyIntent = Intent()

            if (TextUtils.isEmpty(deviceNameTextView.text) || TextUtils.isEmpty(deviceModelTextView.text) || (deviceDateTextView.text == "Date")) {
                if ((deviceNameTextView.text.length > 20) || deviceModelTextView.text.length > 20) {
                    Toast.makeText(
                        this,
                        "Length of username or email can not be more than 20 character",
                        Toast.LENGTH_LONG
                    ).show()
                    setResult(Activity.RESULT_CANCELED, replyIntent)
                }
            } else {
                val deviceName = deviceNameTextView.text.toString()
                val deviceModel = deviceModelTextView.text.toString()
                val deviceDate = deviceDateTextView.text.toString()
                /*val userDetailArr = arrayListOf<String>(username,email,dob)*/
                val userDetailArr = arrayOf<String>(deviceName, deviceModel, deviceDate)
                Log.d("tag", "something " + userDetailArr.get(1))
                replyIntent.putExtra(EXTRA_REPLY, userDetailArr)
                replyIntent.putExtra("deviceImageByteArray", deviceImageByteArray)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
/*calander  ------------------------------------------------------------------------------------------------code*/

        DatePicBtn = findViewById(R.id.pickDeviceDateBtnId)
        deviceDateTextView = findViewById(R.id.deviceAddDateTextViewId)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        Log.d("tag", "in button main activity")

        DatePicBtn.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view: DatePicker, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    Log.d("tag", "in button")
                    deviceDateTextView.setText("" + dayOfMonth + "/ " + monthOfYear + "/ " + year)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
/*calander  ------------------------------------------------------------------------------------------------code*/

        btDeviceCaptureImage = findViewById(R.id.btCaptureDevicePhotoId)
        ivDeviceImage = findViewById(R.id.deviceImageViewId)
        btOpenGallery = findViewById(R.id.btOpenGalleryDeviceId)
        btDeviceCaptureImage.setOnClickListener {
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
            != PackageManager.PERMISSION_GRANTED
        ) {
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
                ivDeviceImage.setImageBitmap(bitmap)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                deviceImageByteArray = stream.toByteArray()
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.getData()
                ivDeviceImage.setImageURI(uri)
                val iStream = uri?.let { getContentResolver().openInputStream(it) }
                deviceImageByteArray = iStream?.let { getBytes(it) }!!
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