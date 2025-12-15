package com.mete.egitici.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.mete.egitici.R
import com.mete.egitici.activities.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setupModuleCards(view)
        return view
    }

    private fun setupModuleCards(view: View) {
        // Create module cards programmatically for now
        val gridLayout = GridLayout(requireContext()).apply {
            columnCount = 2
            rowCount = 4
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }

        val modules = listOf(
            ModuleItem("🗣️ Dil Gelişimi", DilGelisimActivity::class.java),
            ModuleItem("🔢 Matematik", MatematikActivity::class.java),
            ModuleItem("🧠 Bilişsel Gelişim", BilisselActivity::class.java),
            ModuleItem("🎨 Yaratıcılık", YaraticilikActivity::class.java),
            ModuleItem("🔬 Fen Bilgisi", FenBilgisiActivity::class.java),
            ModuleItem("👥 Sosyal Gelişim", SosyalGelisimActivity::class.java),
            ModuleItem("🎮 Oyunlar", OyunlarActivity::class.java),
            ModuleItem("👨‍👩‍👧 Ebeveyn Paneli", EbeveynActivity::class.java)
        )

        modules.forEach { module ->
            val card = createModuleCard(module)
            gridLayout.addView(card)
        }

        // Replace the RecyclerView with GridLayout temporarily
        val parent = view as? ViewGroup
        parent?.removeAllViews()
        parent?.addView(gridLayout)
    }

    private fun createModuleCard(module: ModuleItem): CardView {
        return CardView(requireContext()).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = 400
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }
            radius = 16f
            cardElevation = 8f
            setCardBackgroundColor(resources.getColor(R.color.colorPrimary, null))
            
            val textView = android.widget.TextView(requireContext()).apply {
                text = module.title
                textSize = 18f
                gravity = android.view.Gravity.CENTER
                setTextColor(resources.getColor(android.R.color.white, null))
                setPadding(16, 16, 16, 16)
            }
            
            addView(textView)
            
            setOnClickListener {
                startActivity(Intent(requireContext(), module.activityClass))
            }
        }
    }

    data class ModuleItem(
        val title: String,
        val activityClass: Class<*>
    )
}
