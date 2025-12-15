package com.mete.egitici.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R

class BilisselActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bilissel)
        
        // Initialize cognitive development activities
        setupToolbar()
        loadCognitiveGames()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Bilişsel Gelişim"
    }
    
    private fun loadCognitiveGames() {
        // Load cognitive development games
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
