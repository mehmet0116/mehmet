package com.mete.egitici.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mete.egitici.R
import com.mete.egitici.database.AchievementEntity
import java.text.SimpleDateFormat
import java.util.*

class AchievementAdapter : ListAdapter<AchievementEntity, AchievementAdapter.AchievementViewHolder>(AchievementDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardAchievement)
        private val tvIcon: TextView = itemView.findViewById(R.id.tvAchievementIcon)
        private val tvName: TextView = itemView.findViewById(R.id.tvAchievementName)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvAchievementDescription)
        private val tvReward: TextView = itemView.findViewById(R.id.tvAchievementReward)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvAchievementStatus)
        
        fun bind(achievement: AchievementEntity) {
            tvIcon.text = achievement.icon
            tvName.text = achievement.name
            tvDescription.text = achievement.description
            tvReward.text = "+${achievement.rewardPoints} puan"
            
            if (achievement.isUnlocked) {
                cardView.alpha = 1.0f
                tvStatus.text = "✅ Açıldı"
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
                
                achievement.unlockedDate?.let { date ->
                    val formatter = SimpleDateFormat("dd MMM yyyy", Locale("tr"))
                    tvStatus.append("\n${formatter.format(Date(date))}")
                }
            } else {
                cardView.alpha = 0.5f
                tvStatus.text = "🔒 Kilitli"
                tvStatus.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
                tvStatus.append("\n${achievement.requiredPoints} puan gerekli")
            }
        }
    }
    
    private class AchievementDiffCallback : DiffUtil.ItemCallback<AchievementEntity>() {
        override fun areItemsTheSame(oldItem: AchievementEntity, newItem: AchievementEntity): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: AchievementEntity, newItem: AchievementEntity): Boolean {
            return oldItem == newItem
        }
    }
}
