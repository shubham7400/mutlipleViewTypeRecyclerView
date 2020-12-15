  package com.example.zoomanimation

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

  class MainActivity : AppCompatActivity() {
    var constraintSetHide = ConstraintSet()
      var constraintSetShow = ConstraintSet()
      var isShown:Boolean = false
      lateinit var spaceMain:ConstraintLayout
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spaceMain = findViewById<ConstraintLayout>(R.id.spaceMain)
        constraintSetHide.clone(spaceMain)
        constraintSetShow.clone(this,R.layout.space_detail)
        spaceMain.setOnClickListener {
            handleZoomAction(spaceMain)
        }
    }

      @RequiresApi(Build.VERSION_CODES.KITKAT)
      private fun handleZoomAction(view: View) {
           TransitionManager.beginDelayedTransition(spaceMain)
          if(isShown)
          {
              constraintSetHide.applyTo(spaceMain)
          }else{
              constraintSetShow.applyTo(spaceMain)
          }
          isShown = !isShown
      }
  }