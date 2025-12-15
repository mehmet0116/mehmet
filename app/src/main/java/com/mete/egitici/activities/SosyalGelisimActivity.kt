package com.mete.egitici.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R

class SosyalGelisimActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sosyal_gelisim)
        
        // Initialize social development activities
        setupToolbar()
        loadSocialActivities()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sosyal Gelişim"
    }
    
    private fun loadSocialActivities() {
        // Load social skills activities
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
