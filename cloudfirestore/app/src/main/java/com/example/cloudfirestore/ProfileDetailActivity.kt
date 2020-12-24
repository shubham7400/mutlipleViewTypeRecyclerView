package com.example.cloudfirestore

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cloudfirestore.databinding.ActivityProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var cameraLinearLayout: LinearLayout
    lateinit var galleryLinearLayout: LinearLayout
    private val RequestCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestoreDB = FirebaseFirestore.getInstance()

       /* updateTask()*/

        showCustomSheetDialog()
        binding.imageProfile.setOnClickListener { showDialog() }
        binding.imageCover.setOnClickListener { showDialog() }

    }

    private fun showDialog() {
        bottomSheetDialog.show()
    }

    private fun showCustomSheetDialog() {
        var view = LayoutInflater.from(this).inflate(R.layout.bottum_sheet, null)
        cameraLinearLayout = view.findViewById(R.id.cameraLinearLayout)
        galleryLinearLayout = view.findViewById(R.id.galleryLinearLayout)

        cameraLinearLayout.setOnClickListener(this)
        galleryLinearLayout.setOnClickListener(this)

        bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(view)
    }



    override fun onClick(view: View?) {

        when(view)
        {
            galleryLinearLayout -> {
                Toast.makeText(this, "Share button got clicked", Toast.LENGTH_LONG).show()
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )

                startActivityForResult(intent, RequestCode)
                bottomSheetDialog.dismiss()
            }

            cameraLinearLayout -> {
                Toast.makeText(this, "camera button got clicked", Toast.LENGTH_LONG).show()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                startActivityForResult(intent, RequestCode)
                bottomSheetDialog.dismiss()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(Companion.TAG, "onActivityResult: comming")
        if (resultCode == Activity.RESULT_OK && requestCode == requestCode && data?.data != null){
            val imageUri = data!!.data

        }
    }

    companion object {
        private const val TAG = "ProfileDetailActivity"
    }

}