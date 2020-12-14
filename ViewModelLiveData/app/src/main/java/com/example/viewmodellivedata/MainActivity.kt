package com.example.viewmodellivedata

import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {
    lateinit var addScoreButton: Button
    lateinit var resetScoreButton: Button
    lateinit var scoreTextView: TextView
    lateinit var scoreViewModel: ScoreViewModel
    var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreViewModel = ViewModelProvider(this).get(ScoreViewModel::class.java)

        scoreViewModel.getScore().observe(this, Observer { newScore ->
            scoreTextView.text = newScore.toString()
        })


        scoreTextView = findViewById(R.id.scoreTextView)
        addScoreButton = findViewById(R.id.addScoreButton)
        resetScoreButton = findViewById(R.id.resetScoreButton)
        addScoreButton.setOnClickListener{
            addScore()
        }
        resetScoreButton.setOnClickListener{
            resetScore()
        }
    }

    fun addScore() {
        scoreViewModel.addScore()
    }

    fun resetScore() {
         scoreViewModel.resetScore()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
