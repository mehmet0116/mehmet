package com.mete.egitici.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mete.egitici.R
import com.mete.egitici.database.AppDatabase
import com.mete.egitici.database.DailyChallengeEntity
import com.mete.egitici.utils.DailyChallengeManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Activity to display today's daily challenge
 */
class DailyChallengeActivity : AppCompatActivity() {
    
    private lateinit var tvChallengeTitle: TextView
    private lateinit var tvChallengeDescription: TextView
    private lateinit var tvChallengeReward: TextView
    private lateinit var tvChallengeDifficulty: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    private lateinit var btnStartChallenge: Button
    
    private lateinit var database: AppDatabase
    private lateinit var challengeManager: DailyChallengeManager
    private var currentChallenge: DailyChallengeEntity? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_challenge)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Günlük Görev"
        
        initializeViews()
        database = AppDatabase.getDatabase(this)
        challengeManager = DailyChallengeManager(database.dailyChallengeDao())
        
        loadDailyChallenge()
    }
    
    private fun initializeViews() {
        tvChallengeTitle = findViewById(R.id.tvChallengeTitle)
        tvChallengeDescription = findViewById(R.id.tvChallengeDescription)
        tvChallengeReward = findViewById(R.id.tvChallengeReward)
        tvChallengeDifficulty = findViewById(R.id.tvChallengeDifficulty)
        progressBar = findViewById(R.id.progressChallenge)
        tvProgress = findViewById(R.id.tvProgress)
        btnStartChallenge = findViewById(R.id.btnStartChallenge)
        
        btnStartChallenge.setOnClickListener {
            startChallenge()
        }
    }
    
    private fun loadDailyChallenge() {
        lifecycleScope.launch {
            // Generate today's challenge if not exists
            challengeManager.generateDailyChallenge()
            
            // Load the challenge
            val today = getTodayStartTime()
            database.dailyChallengeDao().getChallengesForDate(today).collect { challenges ->
                if (challenges.isNotEmpty()) {
                    currentChallenge = challenges[0]
                    displayChallenge(challenges[0])
                }
            }
        }
    }
    
    private fun displayChallenge(challenge: DailyChallengeEntity) {
        tvChallengeTitle.text = challenge.title
        tvChallengeDescription.text = challenge.description
        tvChallengeReward.text = "Ödül: ${challenge.rewardPoints} puan 🌟"
        
        val difficultyText = when(challenge.difficulty) {
            "EASY" -> "Kolay 😊"
            "MEDIUM" -> "Orta 😎"
            "HARD" -> "Zor 🔥"
            else -> "Bilinmiyor"
        }
        tvChallengeDifficulty.text = "Zorluk: $difficultyText"
        
        progressBar.max = challenge.targetValue
        progressBar.progress = challenge.currentProgress
        tvProgress.text = "${challenge.currentProgress}/${challenge.targetValue}"
        
        if (challenge.isCompleted) {
            btnStartChallenge.text = "Tamamlandı ✅"
            btnStartChallenge.isEnabled = false
        } else {
            btnStartChallenge.text = "Göreve Başla"
            btnStartChallenge.isEnabled = true
        }
    }
    
    private fun startChallenge() {
        currentChallenge?.let { challenge ->
            Toast.makeText(
                this,
                "Görev başladı! ${challenge.description}",
                Toast.LENGTH_LONG
            ).show()
            
            // Here you would navigate to the appropriate activity/game based on challenge type
            // For now, just show a message
        }
    }
    
    private fun getTodayStartTime(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
