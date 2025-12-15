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

class MatematikActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matematik)
        
        setupToolbar()
        loadMathGames()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🔢 Matematik"
    }
    
    private fun loadMathGames() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        // Load lessons from JSON
        val lessons = loadLessonsFromAssets()
        val mathLessons = lessons.filter { it.category == "math" }
        
        mathLessons.forEach { lesson ->
            val card = createLessonCard(lesson)
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
    
    private fun createLessonCard(lesson: LessonData): CardView {
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
                    text = lesson.title
                    textSize = 18f
                    setTextColor(resources.getColor(R.color.colorPrimary, null))
                    setPadding(0, 0, 0, 8)
                })
                
                addView(TextView(context).apply {
                    text = lesson.description
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                    setPadding(0, 0, 0, 8)
                })
                
                addView(TextView(context).apply {
                    text = "⏱️ ${lesson.duration / 60} dakika • 📊 ${lesson.difficulty.uppercase()}"
                    textSize = 12f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                })
            }
            
            addView(contentLayout)
            
            setOnClickListener {
                Toast.makeText(context, "Ders başlatılıyor: ${lesson.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun loadLessonsFromAssets(): List<LessonData> {
        val lessons = mutableListOf<LessonData>()
        try {
            val jsonString = assets.open("data/lessons.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("lessons")
            
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                lessons.add(LessonData(
                    id = item.getInt("id"),
                    category = item.getString("category"),
                    title = item.getString("title"),
                    duration = item.getInt("duration"),
                    difficulty = item.getString("difficulty"),
                    description = item.optString("description", "")
                ))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return lessons
    }
    
    data class LessonData(
        val id: Int,
        val category: String,
        val title: String,
        val duration: Int,
        val difficulty: String,
        val description: String
    )
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
