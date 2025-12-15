package com.mete.egitici.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mete.egitici.R
import com.mete.egitici.utils.PreferencesHelper

class SettingsFragment : Fragment() {
    
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        preferencesHelper = PreferencesHelper(requireContext())
        setupSettings(view)
        return view
    }

    private fun setupSettings(view: View) {
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
            text = "⚙️ Ayarlar"
            textSize = 24f
            setPadding(0, 0, 0, 32)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })

        // Sound Settings Section
        layout.addView(TextView(requireContext()).apply {
            text = "🔊 Ses Ayarları"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "🔊 Ses Efektleri",
            preferencesHelper.isSoundEffectsEnabled()
        ) { enabled ->
            preferencesHelper.setSoundEffectsEnabled(enabled)
        })
        
        layout.addView(createSetting(
            "🎵 Arka Plan Müziği",
            preferencesHelper.isBackgroundMusicEnabled()
        ) { enabled ->
            preferencesHelper.setBackgroundMusicEnabled(enabled)
        })
        
        // Volume Control
        layout.addView(TextView(requireContext()).apply {
            text = "Ses Seviyesi"
            textSize = 14f
            setPadding(0, 16, 0, 8)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        })
        
        layout.addView(SeekBar(requireContext()).apply {
            max = 100
            progress = (preferencesHelper.getVolume() * 100).toInt()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    preferencesHelper.setVolume(progress / 100f)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        })
        
        // Animation Settings Section
        layout.addView(TextView(requireContext()).apply {
            text = "\n✨ Animasyon Ayarları"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "🎬 Animasyonlar",
            preferencesHelper.isAnimationsEnabled()
        ) { enabled ->
            preferencesHelper.setAnimationsEnabled(enabled)
        })
        
        layout.addView(createSetting(
            "⭐ Parti Efektleri",
            preferencesHelper.isParticleEffectsEnabled()
        ) { enabled ->
            preferencesHelper.setParticleEffectsEnabled(enabled)
        })
        
        // Display Settings Section
        layout.addView(TextView(requireContext()).apply {
            text = "\n📱 Görünüm Ayarları"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "🌙 Karanlık Mod",
            preferencesHelper.isDarkModeEnabled()
        ) { enabled ->
            preferencesHelper.setDarkModeEnabled(enabled)
            Toast.makeText(requireContext(), "Uygulamayı yeniden başlatın", Toast.LENGTH_SHORT).show()
        })
        
        // Text Size
        layout.addView(TextView(requireContext()).apply {
            text = "\nMetin Boyutu"
            textSize = 14f
            setPadding(0, 16, 0, 8)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        })
        
        layout.addView(createTextSizeSelector())
        
        // Accessibility Settings Section
        layout.addView(TextView(requireContext()).apply {
            text = "\n♿ Erişilebilirlik"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "♿ Erişilebilirlik Modu",
            preferencesHelper.isAccessibilityModeEnabled()
        ) { enabled ->
            preferencesHelper.setAccessibilityModeEnabled(enabled)
        })
        
        layout.addView(createSetting(
            "🎨 Yüksek Kontrast",
            preferencesHelper.isHighContrastEnabled()
        ) { enabled ->
            preferencesHelper.setHighContrastEnabled(enabled)
        })
        
        // Parental Control Section
        layout.addView(TextView(requireContext()).apply {
            text = "\n👨‍👩‍👧 Ebeveyn Kontrolü"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "⏱️ Süre Sınırlaması",
            preferencesHelper.isTimeLimitEnabled()
        ) { enabled ->
            preferencesHelper.setTimeLimitEnabled(enabled)
        })
        
        layout.addView(TextView(requireContext()).apply {
            text = "Günlük Kullanım Süresi: ${preferencesHelper.getDailyTimeLimit()} dakika"
            textSize = 14f
            setPadding(0, 8, 0, 8)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
        })
        
        // Notifications Section
        layout.addView(TextView(requireContext()).apply {
            text = "\n🔔 Bildirimler"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 16, 0, 12)
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        })
        
        layout.addView(createSetting(
            "🔔 Bildirimler",
            preferencesHelper.isNotificationsEnabled()
        ) { enabled ->
            preferencesHelper.setNotificationsEnabled(enabled)
        })
        
        layout.addView(createSetting(
            "📅 Günlük Hatırlatmalar",
            preferencesHelper.isDailyRemindersEnabled()
        ) { enabled ->
            preferencesHelper.setDailyRemindersEnabled(enabled)
        })

        val parent = view as? ViewGroup
        parent?.removeAllViews()
        parent?.addView(ScrollView(requireContext()).apply {
            addView(layout)
        })
    }

    private fun createSetting(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit): LinearLayout {
        return LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 12, 0, 12)

            addView(TextView(requireContext()).apply {
                text = title
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
            })

            addView(Switch(requireContext()).apply {
                isChecked = checked
                setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChange(isChecked)
                }
            })
        }
    }
    
    private fun createTextSizeSelector(): RadioGroup {
        return RadioGroup(requireContext()).apply {
            orientation = RadioGroup.HORIZONTAL
            
            val textSizes = listOf("Küçük", "Orta", "Büyük")
            val currentSize = preferencesHelper.getTextSize()
            
            textSizes.forEachIndexed { index, size ->
                addView(RadioButton(requireContext()).apply {
                    text = size
                    id = index
                    isChecked = currentSize == index
                    setOnClickListener {
                        preferencesHelper.setTextSize(index)
                    }
                })
            }
        }
    }
}
