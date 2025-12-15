package com.mete.egitici.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mete.egitici.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setupProfile(view)
        return view
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

        // Profile info
        val profileInfo = listOf(
            "👤 Kullanıcı Profili",
            "",
            "📛 İsim: Küçük Öğrenci",
            "🎂 Yaş: 5",
            "⭐ Toplam Puan: 1250",
            "🏆 Kazanılan Rozetler: 15",
            "📚 Tamamlanan Dersler: 23",
            "🎮 Oynanan Oyunlar: 45",
            "",
            "📊 İlerleme:",
            "Dil Gelişimi: %75",
            "Matematik: %60",
            "Yaratıcılık: %80"
        )

        profileInfo.forEach { info ->
            val textView = TextView(requireContext()).apply {
                text = info
                textSize = if (info.startsWith("👤")) 24f else 16f
                setPadding(0, 8, 0, 8)
                setTextColor(resources.getColor(android.R.color.black, null))
            }
            layout.addView(textView)
        }

        val parent = view as? ViewGroup
        parent?.removeAllViews()
        parent?.addView(layout)
    }
}
