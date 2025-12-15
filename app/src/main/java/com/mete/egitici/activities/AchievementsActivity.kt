package com.mete.egitici.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mete.egitici.R
import com.mete.egitici.database.AppDatabase
import com.mete.egitici.adapters.AchievementAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Activity to display all achievements (locked and unlocked)
 */
class AchievementsActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AchievementAdapter
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievements)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Başarılar"
        
        database = AppDatabase.getDatabase(this)
        setupRecyclerView()
        loadAchievements()
    }
    
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewAchievements)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = AchievementAdapter()
        recyclerView.adapter = adapter
    }
    
    private fun loadAchievements() {
        lifecycleScope.launch {
            database.achievementDao().getAllAchievements().collect { achievements ->
                adapter.submitList(achievements)
                
                val unlockedCount = achievements.count { it.isUnlocked }
                val totalCount = achievements.size
                supportActionBar?.subtitle = "$unlockedCount/$totalCount Açıldı"
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
