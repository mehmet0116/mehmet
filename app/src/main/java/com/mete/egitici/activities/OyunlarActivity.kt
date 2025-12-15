package com.mete.egitici.activities

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mete.egitici.R
import org.json.JSONObject
import java.io.IOException

class OyunlarActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oyunlar)
        
        setupToolbar()
        loadGames()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🎮 Oyunlar"
    }
    
    private fun loadGames() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        // Add Quiz button at the top
        val quizButton = androidx.cardview.widget.CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24)
            }
            radius = 12f
            cardElevation = 8f
            setCardBackgroundColor(resources.getColor(R.color.colorAccent, null))
            
            val buttonLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
                gravity = android.view.Gravity.CENTER
                
                addView(TextView(context).apply {
                    text = "🎯 Bilgi Yarışması Oyna"
                    textSize = 22f
                    setTextColor(resources.getColor(android.R.color.white, null))
                    gravity = android.view.Gravity.CENTER
                })
                
                addView(TextView(context).apply {
                    text = "Sorulara cevap ver ve puan kazan!"
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.white, null))
                    gravity = android.view.Gravity.CENTER
                    setPadding(0, 8, 0, 0)
                })
            }
            
            addView(buttonLayout)
            
            setOnClickListener {
                startActivity(android.content.Intent(context, QuizActivity::class.java))
            }
        }
        layout.addView(quizButton)
        
        // Load games from JSON
        val games = loadGamesFromAssets()
        
        games.forEach { game ->
            val card = createGameCard(game)
            layout.addView(card)
        }
        
        val scrollView = android.widget.ScrollView(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(layout)
        }
        
        val parent = findViewById<ViewGroup>(android.R.id.content)
        parent.removeAllViews()
        parent.addView(scrollView)
    }
    
    private fun createGameCard(game: GameData): CardView {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 12f
            cardElevation = 4f
            setCardBackgroundColor(resources.getColor(android.R.color.white, null))
            
            val contentLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 24, 24, 24)
                
                addView(TextView(context).apply {
                    text = "${game.icon} ${game.name}"
                    textSize = 18f
                    setTextColor(resources.getColor(R.color.colorPrimary, null))
                    setPadding(0, 0, 0, 8)
                })
                
                addView(TextView(context).apply {
                    text = game.description
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    setPadding(0, 0, 0, 8)
                })
                
                addView(TextView(context).apply {
                    text = "👶 ${game.minAge}-${game.maxAge} yaş • 📊 ${game.difficulty.uppercase()}"
                    textSize = 12f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                })
            }
            
            addView(contentLayout)
            
            setOnClickListener {
                Toast.makeText(context, "Oyun başlatılıyor: ${game.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadGamesFromAssets(): List<GameData> {
        val games = mutableListOf<GameData>()
        try {
            val jsonString = assets.open("data/games.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("games")
            
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                games.add(GameData(
                    id = item.getInt("id"),
                    name = item.getString("name"),
                    category = item.getString("category"),
                    difficulty = item.getString("difficulty"),
                    minAge = item.getInt("minAge"),
                    maxAge = item.getInt("maxAge"),
                    description = item.getString("description"),
                    icon = item.optString("icon", "🎮")
                ))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return games
    }
    
    data class GameData(
        val id: Int,
        val name: String,
        val category: String,
        val difficulty: String,
        val minAge: Int,
        val maxAge: Int,
        val description: String,
        val icon: String
    )
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
