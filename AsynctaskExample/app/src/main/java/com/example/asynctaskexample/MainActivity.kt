package com.example.asynctaskexample

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressBarIndicator: ProgressBar
    lateinit var textView: TextView

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)
        progressBarIndicator = findViewById(R.id.progressbarIndicater)
        textView = findViewById(R.id.textView)
        progressBarIndicator.visibility = View.GONE

        button.setOnClickListener {
            updateTask()
        }
    }

    private fun updateTask() {
        Log.i(TAG, "updateTask: "+Thread.currentThread().name)
        val updataTask = UpdataTask()
        updataTask.execute("doInBackground: method is running")//this string will go in parameter of doInBackground method
    }
 // first of all onPreExecute method gets run and after that doInBackground gets run and than onProgressUpdate method gets run and in last onPostExecute method gets run

    // asyncTask is used to run code on background thread instead of main thread because high load on main thread cause UI hanging
    inner class UpdataTask: AsyncTask<String, Int, String>() {
        override fun onPreExecute() {
            Log.i(TAG, "onPreExecute: onPreExecute "+Thread.currentThread().name)
            progressBarIndicator.visibility = View.VISIBLE
            button.isEnabled = false
            textView.text = "Uploading data ..."
        }
        override fun doInBackground(vararg params: String?): String? {
            Log.i(TAG, "doInBackground: "+Thread.currentThread().name+" "+params[0])
            for (i in 1..10)
            {
                Thread.sleep(500)
                publishProgress(i) // this value of i will go in parameter of OnProgressUpdate method
            }
            return "Task got completed" // this string will go in parameter of onPostExecute method
        }

        override fun onProgressUpdate(vararg values: Int?) {
            Log.i(TAG, "onProgressUpdate: "+values[0]+"  "+Thread.currentThread().name)
            values[0]?.let { progressBar.setProgress(it.plus(0) ) }
        }
        override fun onPostExecute(result: String?) {
            Log.i(TAG, "onPreExecute: onPostExecute "+Thread.currentThread().name)
            textView.text = result.toString()
            progressBarIndicator.visibility = View.GONE
            button.isEnabled = true
        }
    }

}