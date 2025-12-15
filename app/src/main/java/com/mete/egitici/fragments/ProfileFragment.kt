package com.mete.egitici.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mete.egitici.R
import com.mete.egitici.activities.AchievementsActivity
import com.mete.egitici.activities.DailyChallengeActivity
import com.mete.egitici.database.AppDatabase
import com.mete.egitici.database.UserStatisticsEntity
import com.mete.egitici.utils.AchievementManager
import com.mete.egitici.utils.DailyChallengeManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    
    private lateinit var database: AppDatabase
    private lateinit var achievementManager: AchievementManager
    private lateinit var challengeManager: DailyChallengeManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        
        database = AppDatabase.getDatabase(requireContext())
        achievementManager = AchievementManager(requireContext(), database.achievementDao())
        challengeManager = DailyChallengeManager(database.dailyChallengeDao())
        
        setupProfile(view)
        initializeData()
        
        return view
    }
    
    private fun initializeData() {
        lifecycleScope.launch {
            // Initialize achievements if needed
            achievementManager.initializeAchievements()
            
            // Generate today's challenge
            challengeManager.generateDailyChallenge()
            
            // Initialize user statistics if not exists
            val stats = database.userStatisticsDao().getUserStatisticsOnce()
            if (stats == null) {
                database.userStatisticsDao().insertStatistics(
                    UserStatisticsEntity(
                        userId = "default_user",
                        totalPoints = 0,
                        level = 1
                    )
                )
            }
        }
    }

    private fun setupProfile(view: View) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
        }

        // Title
        layout.addView(TextView(requireContext()).apply {
            text = "👤 Kullanıcı Profili"
            textSize = 24f
            setPadding(0, 8, 0, 16)
            setTextColor(resources.getColor(android.R.color.black, null))
        })
        
        // Statistics container
        val statsContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 16, 0, 16)
        }
        layout.addView(statsContainer)
        
        // Load and display statistics
        lifecycleScope.launch {
            database.userStatisticsDao().getUserStatistics().collect { stats ->
                statsContainer.removeAllViews()
                
                if (stats != null) {
                    val statsInfo = listOf(
                        "📛 İsim: Küçük Öğrenci",
                        "🎂 Yaş: 5",
                        "🌟 Seviye: ${stats.level}",
                        "⭐ Toplam Puan: ${stats.totalPoints}",
                        "🏆 Açılan Başarılar: ${stats.achievementsUnlocked}",
                        "📚 Tamamlanan Dersler: ${stats.totalLessonsCompleted}",
                        "🎮 Oynanan Oyunlar: ${stats.totalGamesPlayed}",
                        "✅ Kazanılan Oyunlar: ${stats.totalGamesWon}",
                        "🎯 Mükemmel Skorlar: ${stats.perfectScores}",
                        "🔥 Güncel Seri: ${stats.currentStreak} gün",
                        "📈 En Uzun Seri: ${stats.longestStreak} gün"
                    )
                    
                    statsInfo.forEach { info ->
                        statsContainer.addView(TextView(requireContext()).apply {
                            text = info
                            textSize = 16f
                            setPadding(0, 8, 0, 8)
                            setTextColor(resources.getColor(android.R.color.black, null))
                        })
                    }
                } else {
                    statsContainer.addView(TextView(requireContext()).apply {
                        text = "İstatistikler yükleniyor..."
                        textSize = 16f
                    })
                }
            }
        }
        
        // Buttons container
        val buttonsContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 24, 0, 0)
        }
        layout.addView(buttonsContainer)
        
        // Achievements button
        buttonsContainer.addView(Button(requireContext()).apply {
            text = "🏆 Başarılarım"
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnClickListener {
                startActivity(Intent(requireContext(), AchievementsActivity::class.java))
            }
        })
        
        // Daily challenge button
        buttonsContainer.addView(Button(requireContext()).apply {
            text = "🎯 Günlük Görev"
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnClickListener {
                startActivity(Intent(requireContext(), DailyChallengeActivity::class.java))
            }
        })
        
        // Statistics button
        buttonsContainer.addView(Button(requireContext()).apply {
            text = "📊 Detaylı İstatistikler"
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnClickListener {
                startActivity(Intent(requireContext(), com.mete.egitici.activities.StatisticsActivity::class.java))
            }
        })
        
        // Leaderboard button
        buttonsContainer.addView(Button(requireContext()).apply {
            text = "🏆 Liderlik Tablosu"
            textSize = 18f
            setPadding(16, 16, 16, 16)
            setOnClickListener {
                startActivity(Intent(requireContext(), com.mete.egitici.activities.LeaderboardActivity::class.java))
            }
        })

        val parent = view as? ViewGroup
        parent?.removeAllViews()
        parent?.addView(layout)
    }
}
