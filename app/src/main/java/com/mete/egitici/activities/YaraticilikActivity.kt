package com.mete.egitici.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R

class YaraticilikActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yaraticilik)
        
        // Initialize creativity activities
        setupToolbar()
        loadCreativeTools()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Yaratıcılık"
    }
    
    private fun loadCreativeTools() {
        // Load drawing, music, and creative activities
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
