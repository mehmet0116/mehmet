package com.mete.egitici.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R

class OyunlarActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oyunlar)
        
        // Initialize games
        setupToolbar()
        loadGames()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Oyunlar"
    }
    
    private fun loadGames() {
        // Load various educational games
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
