package com.mete.egitici.activities

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mete.egitici.R

class EbeveynActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebeveyn)
        
        setupToolbar()
        loadParentPanel()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "👨‍👩‍👧 Ebeveyn Kontrol Paneli"
    }
    
    private fun loadParentPanel() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }
        
        // Statistics
        layout.addView(createStatsCard(
            "📊 İstatistikler",
            listOf(
                "Bugünkü kullanım: 45 dakika",
                "Bu hafta: 4 saat 30 dakika",
                "Tamamlanan dersler: 23",
                "Kazanılan rozetler: 15"
            )
        ))
        
        // Progress
        layout.addView(createStatsCard(
            "📈 İlerleme Raporu",
            listOf(
                "Dil Gelişimi: %75",
                "Matematik: %60",
                "Yaratıcılık: %80",
                "Fen Bilgisi: %55"
            )
        ))
        
        // Settings
        layout.addView(createStatsCard(
            "⚙️ Ayarlar",
            listOf(
                "Günlük süre sınırı: 60 dakika",
                "İçerik seviyesi: Orta",
                "Bildirimler: Açık",
                "Raporlar: E-posta ile gönder"
            )
        ))
        
        val scrollView = android.widget.ScrollView(this).apply {
            addView(layout)
        }
        
        val parent = findViewById<ViewGroup>(android.R.id.content)
        parent.removeAllViews()
        parent.addView(scrollView)
    }
    
    private fun createStatsCard(title: String, items: List<String>): CardView {
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
                    text = title
                    textSize = 20f
                    setTextColor(resources.getColor(R.color.colorPrimary, null))
                    setPadding(0, 0, 0, 16)
                })
                
                items.forEach { item ->
                    addView(TextView(context).apply {
                        text = "• $item"
                        textSize = 14f
                        setTextColor(resources.getColor(android.R.color.black, null))
                        setPadding(0, 4, 0, 4)
                    })
                }
            }
            
            addView(contentLayout)
            
            setOnClickListener {
                Toast.makeText(context, "Detaylar için tıklandı", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
