package com.mete.egitici

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mete.egitici.activities.*

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setupNavigation()
    }
    
    private fun setupNavigation() {
        findViewById<BottomNavigationView>(R.id.bottomNavigation)?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Load home fragment
                    true
                }
                R.id.nav_profile -> {
                    // Load profile fragment
                    true
                }
                R.id.nav_settings -> {
                    // Load settings fragment
                    true
                }
                else -> false
            }
        }
    }
    
    fun openActivity(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }
}
