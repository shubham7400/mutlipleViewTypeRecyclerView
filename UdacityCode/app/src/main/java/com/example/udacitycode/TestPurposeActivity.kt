package com.example.udacitycode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout

class TestPurposeActivity : AppCompatActivity() {
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Instantiate an ImageView and define its properties
        val i = ImageView(this).apply {
            setImageResource(R.drawable.about_android_trivia)
            contentDescription = resources.getString(R.string.android_trivia)

            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        // Create a ConstraintLayout in which to add the ImageView
        constraintLayout = ConstraintLayout(this).apply {

            // Add the ImageView to the layout.
            addView(i)
        }

        // Set the layout as the content view.
        setContentView(constraintLayout)
    }
}