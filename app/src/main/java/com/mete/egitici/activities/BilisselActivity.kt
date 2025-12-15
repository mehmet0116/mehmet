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

class BilisselActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bilissel)
        
        setupToolbar()
        loadCognitiveGames()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🧠 Bilişsel Gelişim"
    }
    
    private fun loadCognitiveGames() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        val activities = listOf(
            ActivityItem("🧩 Hafıza Oyunları", "Eşleştirme ve hafıza kartları ile hafızanı güçlendir"),
            ActivityItem("🎯 Mantık Bulmacaları", "Mantık ve akıl yürütme yeteneklerini geliştir"),
            ActivityItem("👁️ Dikkat Egzersizleri", "Konsantrasyonunu artır, detayları fark et"),
            ActivityItem("🔢 Sınıflandırma", "Nesneleri gruplara ayır ve düzenle"),
            ActivityItem("🎨 Örüntü Tamamlama", "Örüntüleri tanı ve tamamla"),
            ActivityItem("🌀 Labirent Oyunları", "Yolunu bul ve hedefe ulaş")
        )
        
        activities.forEach { activity ->
            layout.addView(createActivityCard(activity))
        }
        
        val scrollView = android.widget.ScrollView(this).apply {
            addView(layout)
        }
        
        val parent = findViewById<ViewGroup>(android.R.id.content)
        parent.removeAllViews()
        parent.addView(scrollView)
    }
    
    private fun createActivityCard(activity: ActivityItem): CardView {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 12f
            cardElevation = 4f
            
            val contentLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 24, 24, 24)
                
                addView(TextView(context).apply {
                    text = activity.title
                    textSize = 18f
                    setTextColor(resources.getColor(R.color.colorPrimary, null))
                    setPadding(0, 0, 0, 8)
                })
                
                addView(TextView(context).apply {
                    text = activity.description
                    textSize = 14f
                    setTextColor(resources.getColor(android.R.color.darker_gray, null))
                })
            }
            
            addView(contentLayout)
            
            setOnClickListener {
                Toast.makeText(context, "Aktivite başlatılıyor: ${activity.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    data class ActivityItem(val title: String, val description: String)
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
