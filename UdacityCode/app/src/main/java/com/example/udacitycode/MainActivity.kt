package com.example.udacitycode

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.udacitycode.databinding.ActivityMainBinding
import android.content.Intent



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val myName: MyName = MyName("shubham mogarkar bairagi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding.myName = myName

        binding.textView.setOnClickListener {
            val intent = Intent(this, ConstraintLayoutActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener { it ->
            addNickname(it)
        }
    }

    private fun addNickname(it: View?) {
        /*binding.textView.text = binding.nicknameEditText.text*/
        binding.apply {
            myName?.nickname = nicknameEditText.text.toString()
            invalidateAll()
            nicknameEditText.visibility = View.GONE
            if (it != null) {
                it.visibility = View.GONE
            }
            nicknameTextView.visibility = View.VISIBLE
        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}