package com.example.cloudfirestore


import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.cloudfirestore.databinding.ActivityProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private var firestoreDB: FirebaseFirestore? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var cameraLinearLayout: LinearLayout
    lateinit var galleryLinearLayout: LinearLayout
    lateinit var imageUri: Uri
    lateinit var storage: FirebaseStorage
    private var imageChecker: String = "cover"
    val permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        storage = Firebase.storage

        auth = Firebase.auth
        firestoreDB = FirebaseFirestore.getInstance()
        updateTask()
        showCustomSheetDialog()
        binding.imageProfile.setOnClickListener {
            imageChecker = "profile"
            showDialog()
        }
        binding.imageCover.setOnClickListener {
            imageChecker = "cover"
            showDialog()
        }
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
            cameraLinearLayout -> {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                    intent.resolveActivity(packageManager)?.also {
                        startActivityForResult(intent, 1)
                    }
                }
                bottomSheetDialog.dismiss()
            }

            galleryLinearLayout -> {
                Toast.makeText(this, "camera button got clicked", Toast.LENGTH_LONG).show()
                task.execute(null,null,null)
                bottomSheetDialog.dismiss()
            }

        }
    }

    override fun onStart() {
        super.onStart()
            requestPermission()
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(this, permissions, 0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val bitmap = data?.extras?.get("data") as Bitmap
                Log.i(TAG, "onActivityResult: camer urr " + bitmap.toString())
                bitmapToUri(bitmap)
            }
            else if (requestCode ==  2) {
                val uri = data?.getData()
                Log.i(TAG, "onActivityResult: gallery " + uri.toString())
                if (uri != null) {
                    uploadImageIntoDatabase(uri)
                }
            }
        }
    }

    private fun bitmapToUri(bitmap: Bitmap)
    {
        Log.i(TAG, "bitmapToUri: getting called")
        val format =  Bitmap.CompressFormat.PNG
        val os = ByteArrayOutputStream()
        bitmap.compress(format, 1, os)
        val path: String = MediaStore.Images.Media.insertImage(contentResolver, bitmap, System.currentTimeMillis().toString() + ".png", null)
        val imageUri = Uri.parse(path)
        Log.i(TAG, "onActivityResult: image urii " + imageUri)
        uploadImageIntoDatabase(imageUri)
    }

    private fun uploadImageIntoDatabase(uri: Uri) {
        Log.i(TAG, "uploadImageIntoDatabase: yes comming here")
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("image is uploading ,Please wait ...")
        progressBar.show()
        Log.i(TAG, "uploadImageIntoDatabase: yes comming here too")
        if(uri != null)
        {
            var storageRef = storage.reference.child("images").child(System.currentTimeMillis().toString() + ".png")
            val uploadTask = storageRef.putFile(uri)

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                // ...
                Log.i(TAG, "uploadImageIntoDatabase: image link "+taskSnapshot.toString())
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
                var metadata = taskSnapshot.metadata?.reference?.downloadUrl
                if (metadata != null) {
                    metadata.addOnSuccessListener {
                        val imageLink = it.toString()
                        Log.i(TAG, "uploadImageIntoDatabase:  metadata " + imageLink)
                        var imageHashMap = HashMap<String, Any>()
                        if (imageChecker == "cover")
                        {
                            imageHashMap["cover"] = imageLink
                        }else{
                            imageHashMap["profile"] = imageLink
                        }
                        firestoreDB?.collection("Profile")?.document(auth.currentUser!!.uid)?.update(imageHashMap)
                                ?.addOnSuccessListener {
                                    Toast.makeText(this, imageChecker +" image has been changed", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this,MainActivity::class.java)
                                    startActivity(intent)
                                }
                            ?.addOnFailureListener { e ->
                                Log.i(TAG, "uploadImageIntoDatabase: error occured while changing "+imageChecker+" image")
                            }
                    }
                }
            }

    progressBar.dismiss()
        }
    }

    private fun updateTask() {
        Log.i(TAG, "updateTask: " + Thread.currentThread().name)
        val updataTask = UpdataTask()
        updataTask.execute("doInBackground: method is running")//this string will go in parameter of doInBackground method
    }

    inner class UpdataTask: AsyncTask<String, Int, String>() {
        override fun onPreExecute() {
            Log.i(TAG, "onPreExecute: onPreExecute " + Thread.currentThread().name)
            binding.progressbarIndicater.visibility = View.VISIBLE

        }
        override fun doInBackground(vararg params: String?): String? {
            Log.i(TAG, "doInBackground: " + Thread.currentThread().name + " " + params[0])
            Log.i(TAG, "setProfile: got called")
            firestoreDB!!.collection("Profile").get()
                    .addOnCompleteListener { allProfile ->
                        if (allProfile.isSuccessful)
                        {
                            Log.i(TAG, "setProfile: successful")
                            for (profile in allProfile.result!!)
                            {
                                if (profile["uid"] == auth.currentUser!!.uid)
                                {
                                    Log.i(
                                            TAG,
                                            "setProfile: found user " + profile["username"] + " dd " + profile["uid"] + " dd " + profile["profile"] + " dd " + profile["cover"]
                                    )
                                    binding.usernameProfile.text = profile["username"].toString()
                                    Picasso.get().load(profile["profile"].toString()).into(binding.imageProfile)
                                    Picasso.get().load(profile["cover"].toString()).into(binding.imageCover)
                                    Log.i(TAG, "doInBackground: after this")
                                }
                            }
                        }else {
                            Log.d(TAG, "Error getting documents: ", allProfile.exception)
                        }
                    }

            Log.i(TAG, "doInBackground: check " + binding.usernameProfile)
            return ""
        }


        override fun onPostExecute(result: String?) {
            Log.i(TAG, "onPreExecute: onPostExecute " + Thread.currentThread().name)
            binding.progressbarIndicater.visibility = View.GONE
        }


    }

    var task: AsyncTask<Void?, Void?, Void?> = object : AsyncTask<Void?, Void?, Void?>() {





        override fun doInBackground(vararg params: Void?): Void? {
            Intent(Intent.ACTION_GET_CONTENT).also { intent ->
                intent.type = "image/*"
                intent.resolveActivity(packageManager)?.also {
                    startActivityForResult(intent, 2)
                }
            }
            return null
        }
    }


    companion object {
        private const val TAG = "ProfileActivity"
    }
}

