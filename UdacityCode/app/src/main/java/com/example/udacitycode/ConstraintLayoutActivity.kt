package com.example.udacitycode

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.udacitycode.databinding.ActivityConstraintLayoutBinding
import com.example.udacitycode.databinding.ActivityMainBinding

class ConstraintLayoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstraintLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_constraint_layout)

        setListeners()
    }

    private fun setListeners() {
         val clickableViews: List<View> = listOf(binding.oneTv,binding.towTv,binding.treeTv,binding.fourTv,binding.fiveTv)

        for (view in clickableViews)
        {
            view.setOnClickListener { makeColored(it) }
        }
    }

    private fun makeColored( view: View?) {
        when(view!!.id)
        {
            R.id.one_tv -> view.setBackgroundColor(Color.BLACK)
            R.id.tow_tv -> view.setBackgroundColor(Color.GREEN)
            R.id.tree_tv -> view.setBackgroundColor(Color.CYAN)
            R.id.four_tv -> view.setBackgroundColor(Color.RED)
            R.id.five_tv -> view.setBackgroundColor(Color.DKGRAY)
        }
    }
}