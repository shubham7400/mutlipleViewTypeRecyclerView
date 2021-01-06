package com.example.udacitycode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.udacitycode.databinding.ActivityAndroidTriviaBinding

class AndroidTriviaActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAndroidTriviaBinding>(this,R.layout.activity_android_trivia)
        drawerLayout = binding.drawerLayout
        val navcontroller =  this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navcontroller,drawerLayout)

        navcontroller.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _ ->
             if (nd.id == nc.graph.startDestination)
             {
                 drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
             }else{
                 drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
             }
        }
        NavigationUI.setupWithNavController(binding.navView,navcontroller)
    }

    override fun onSupportNavigateUp(): Boolean {
         val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }
}