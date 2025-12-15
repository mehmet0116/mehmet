package com.mete.egitici.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R

class DilGelisimActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dil_gelisim)
        
        // Initialize language development activities
        setupToolbar()
        loadActivities()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dil Gelişimi"
    }
    
    private fun loadActivities() {
        // Load language learning games and activities
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
