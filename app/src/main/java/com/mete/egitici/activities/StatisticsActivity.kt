package com.mete.egitici.activities

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mete.egitici.R
import com.mete.egitici.database.AppDatabase
import com.mete.egitici.utils.LevelManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Detailed statistics and progress tracking activity
 */
class StatisticsActivity : AppCompatActivity() {
    
    private lateinit var database: AppDatabase
    private lateinit var levelManager: LevelManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "📊 İstatistikler"
        
        database = AppDatabase.getDatabase(this)
        levelManager = LevelManager(database.userStatisticsDao())
        
        loadStatistics()
    }
    
    private fun loadStatistics() {
        lifecycleScope.launch {
            database.userStatisticsDao().getUserStatistics().collect { stats ->
                stats?.let {
                    displayStatistics(it)
                }
            }
        }
    }
    
    private fun displayStatistics(stats: com.mete.egitici.database.UserStatisticsEntity) {
        val container = findViewById<LinearLayout>(R.id.statsContainer)
        container.removeAllViews()
        
        // Level and XP Section
        container.addView(createSectionHeader("🌟 Seviye ve Deneyim"))
        container.addView(createStatRow("Mevcut Seviye", "${stats.level}"))
        container.addView(createStatRow("Toplam XP", "${stats.experiencePoints}"))
        
        val progressPercent = levelManager.getProgressToNextLevel(stats.level, stats.experiencePoints)
        container.addView(createStatRow("Sonraki Seviyeye", "%.1f%%".format(progressPercent)))
        
        // Points Section
        container.addView(createSectionHeader("⭐ Puanlar"))
        container.addView(createStatRow("Toplam Puan", "${stats.totalPoints}"))
        
        // Games Section
        container.addView(createSectionHeader("🎮 Oyunlar"))
        container.addView(createStatRow("Oynanan Oyunlar", "${stats.totalGamesPlayed}"))
        container.addView(createStatRow("Kazanılan Oyunlar", "${stats.totalGamesWon}"))
        
        val winRate = if (stats.totalGamesPlayed > 0) {
            (stats.totalGamesWon.toFloat() / stats.totalGamesPlayed.toFloat() * 100)
        } else 0f
        container.addView(createStatRow("Kazanma Oranı", "%.1f%%".format(winRate)))
        
        // Lessons Section
        container.addView(createSectionHeader("📚 Dersler"))
        container.addView(createStatRow("Tamamlanan Dersler", "${stats.totalLessonsCompleted}"))
        
        // Performance Section
        container.addView(createSectionHeader("🎯 Performans"))
        container.addView(createStatRow("Ortalama Skor", "%.1f".format(stats.averageScore)))
        container.addView(createStatRow("Mükemmel Skorlar", "${stats.perfectScores}"))
        
        // Achievements Section
        container.addView(createSectionHeader("🏆 Başarılar"))
        container.addView(createStatRow("Açılan Başarılar", "${stats.achievementsUnlocked}"))
        
        // Streak Section
        container.addView(createSectionHeader("🔥 Seriler"))
        container.addView(createStatRow("Güncel Seri", "${stats.currentStreak} gün"))
        container.addView(createStatRow("En Uzun Seri", "${stats.longestStreak} gün"))
        
        // Time Section
        container.addView(createSectionHeader("⏱️ Süre"))
        val hours = stats.totalTimeSpent / (1000 * 60 * 60)
        val minutes = (stats.totalTimeSpent / (1000 * 60)) % 60
        container.addView(createStatRow("Toplam Süre", "${hours}s ${minutes}d"))
        
        // Favorite Section
        container.addView(createSectionHeader("❤️ Favoriler"))
        container.addView(createStatRow("Favori Kategori", stats.favoriteCategory.ifEmpty { "Henüz yok" }))
    }
    
    private fun createSectionHeader(title: String): TextView {
        return TextView(this).apply {
            text = title
            textSize = 20f
            setTextColor(getColor(android.R.color.black))
            setPadding(0, 24, 0, 12)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }
    
    private fun createStatRow(label: String, value: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 8, 0, 8)
            
            addView(TextView(this@StatisticsActivity).apply {
                text = label
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setTextColor(getColor(android.R.color.darker_gray))
            })
            
            addView(TextView(this@StatisticsActivity).apply {
                text = value
                textSize = 18f
                setTextColor(getColor(android.R.color.black))
                setTypeface(null, android.graphics.Typeface.BOLD)
            })
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
