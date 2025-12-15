package com.mete.egitici

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.utils.SoundManager

class WelcomeActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        
        // Set welcome message
        findViewById<TextView>(R.id.tvWelcome)?.text = getString(R.string.msg_welcome_mete)
        
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
