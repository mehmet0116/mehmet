package com.mete.egitici.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mete.egitici.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
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
            setTextColor(resources.getColor(android.R.color.black, null))
        })

        // Sound Settings
        layout.addView(createSetting("🔊 Ses Efektleri", true))
        layout.addView(createSetting("🎵 Arka Plan Müziği", true))
        
        // Display Settings
        layout.addView(TextView(requireContext()).apply {
            text = "\n📱 Görünüm"
            textSize = 18f
            setPadding(0, 16, 0, 8)
            setTextColor(resources.getColor(android.R.color.black, null))
        })
        layout.addView(createSetting("🌙 Karanlık Mod", false))
        layout.addView(createSetting("♿ Erişilebilirlik Modu", false))
        
        // Difficulty
        layout.addView(TextView(requireContext()).apply {
            text = "\n🎯 Zorluk Seviyesi: Orta"
            textSize = 16f
            setPadding(0, 16, 0, 8)
            setTextColor(resources.getColor(android.R.color.black, null))
        })
        
        // Parent Control
        layout.addView(TextView(requireContext()).apply {
            text = "\n👨‍👩‍👧 Ebeveyn Kontrolü"
            textSize = 18f
            setPadding(0, 16, 0, 8)
            setTextColor(resources.getColor(android.R.color.black, null))
        })
        layout.addView(TextView(requireContext()).apply {
            text = "⏱️ Günlük Kullanım Süresi: 60 dakika"
            textSize = 14f
            setPadding(0, 8, 0, 8)
            setTextColor(resources.getColor(android.R.color.darker_gray, null))
        })

        val parent = view as? ViewGroup
        parent?.removeAllViews()
        parent?.addView(layout)
    }

    private fun createSetting(title: String, checked: Boolean): LinearLayout {
        return LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 8, 0, 8)

            addView(TextView(requireContext()).apply {
                text = title
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                setTextColor(resources.getColor(android.R.color.black, null))
            })

            addView(Switch(requireContext()).apply {
                isChecked = checked
            })
        }
    }
}
