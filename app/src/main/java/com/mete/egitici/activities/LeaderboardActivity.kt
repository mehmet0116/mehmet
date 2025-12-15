package com.mete.egitici.activities

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.mete.egitici.R
import com.mete.egitici.database.AppDatabase
import kotlinx.coroutines.launch

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var containerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupToolbar()
        createUI()
        loadLeaderboard()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🏆 Liderlik Tablosu"
    }

    private fun createUI() {
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
            setBackgroundColor(resources.getColor(android.R.color.white, null))
        }

        // Header
        val header = TextView(this).apply {
            text = "🌟 En İyi Skorlar 🌟"
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(resources.getColor(R.color.colorPrimary, null))
            setPadding(0, 16, 0, 24)
        }
        mainLayout.addView(header)

        // Container for leaderboard items
        containerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val scrollView = android.widget.ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            addView(containerLayout)
        }
        mainLayout.addView(scrollView)

        // Reset button
        val btnReset = Button(this).apply {
            text = "🔄 İstatistikleri Sıfırla"
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 0)
            }
            setOnClickListener {
                showResetConfirmation()
            }
        }
        mainLayout.addView(btnReset)

        setContentView(mainLayout)
    }

    private fun loadLeaderboard() {
        lifecycleScope.launch {
            val stats = database.userStatisticsDao().getUserStatisticsOnce()
            
            if (stats != null) {
                addLeaderboardItem("🌟 Toplam Puan", stats.totalPoints.toString(), 1)
                addLeaderboardItem("📊 Seviye", stats.level.toString(), 2)
                addLeaderboardItem("🎯 Mükemmel Skorlar", stats.perfectScores.toString(), 3)
                addLeaderboardItem("🎮 Kazanılan Oyunlar", stats.totalGamesWon.toString(), 4)
                addLeaderboardItem("📚 Tamamlanan Dersler", stats.totalLessonsCompleted.toString(), 5)
                addLeaderboardItem("🔥 En Uzun Seri", "${stats.longestStreak} gün", 6)
                addLeaderboardItem("🏆 Açılan Başarılar", stats.achievementsUnlocked.toString(), 7)
                addLeaderboardItem("⏱️ Toplam Süre", "${stats.totalTimeSpent / 60} dakika", 8)
            } else {
                // Show empty state
                containerLayout.addView(TextView(this@LeaderboardActivity).apply {
                    text = "Henüz istatistik yok.\nOyun oynamaya başla!"
                    textSize = 18f
                    gravity = Gravity.CENTER
                    setPadding(32, 64, 32, 64)
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                })
            }
        }
    }

    private fun addLeaderboardItem(title: String, value: String, rank: Int) {
        val card = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 12)
            }
            radius = 12f
            cardElevation = 4f
            
            // Highlight top 3
            val bgColor = when(rank) {
                1 -> android.graphics.Color.parseColor("#FFD700") // Gold
                2 -> android.graphics.Color.parseColor("#C0C0C0") // Silver
                3 -> android.graphics.Color.parseColor("#CD7F32") // Bronze
                else -> resources.getColor(android.R.color.white, null)
            }
            setCardBackgroundColor(bgColor)
        }

        val itemLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(20, 20, 20, 20)
            gravity = Gravity.CENTER_VERTICAL
        }

        // Rank
        val rankText = TextView(this).apply {
            text = "#$rank"
            textSize = 20f
            setTextColor(resources.getColor(R.color.colorPrimary, null))
            layoutParams = LinearLayout.LayoutParams(80, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        itemLayout.addView(rankText)

        // Title and value
        val infoLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }

        infoLayout.addView(TextView(this).apply {
            text = title
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black, null))
        })

        infoLayout.addView(TextView(this).apply {
            text = value
            textSize = 24f
            setTextColor(resources.getColor(R.color.colorAccent, null))
            setPadding(0, 4, 0, 0)
        })

        itemLayout.addView(infoLayout)
        card.addView(itemLayout)
        containerLayout.addView(card)
    }

    private fun showResetConfirmation() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("⚠️ Uyarı")
            .setMessage("Tüm istatistikler sıfırlanacak. Emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                resetStatistics()
            }
            .setNegativeButton("Hayır", null)
            .show()
    }

    private fun resetStatistics() {
        lifecycleScope.launch {
            database.userStatisticsDao().deleteAll()
            containerLayout.removeAllViews()
            loadLeaderboard()
            android.widget.Toast.makeText(
                this@LeaderboardActivity,
                "İstatistikler sıfırlandı",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
