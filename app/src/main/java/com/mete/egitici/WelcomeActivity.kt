package com.mete.egitici

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.utils.SoundManager

class WelcomeActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        
        // Play welcome sound if available
        SoundManager.playWelcomeSound(this)
        
        findViewById<Button>(R.id.btnStart)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        SoundManager.release()
    }
}
