package com.mete.egitici.activities

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mete.egitici.R

class MemoryGameActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var tvScore: TextView
    private lateinit var tvMoves: TextView
    private var cards = mutableListOf<MemoryCard>()
    private var flippedCards = mutableListOf<Int>()
    private var matchedPairs = 0
    private var moves = 0
    private var score = 0
    private val totalPairs = 8
    
    // Emoji symbols for cards
    private val symbols = listOf(
        "🍎", "🍌", "🍇", "🍊", "🍓", "🍒", "🥝", "🍑"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupToolbar()
        createUI()
        initializeGame()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🎴 Hafıza Oyunu"
    }

    private fun createUI() {
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        // Score header
        val headerCard = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 12f
            cardElevation = 4f
            setCardBackgroundColor(resources.getColor(R.color.colorPrimary, null))
        }

        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
            gravity = Gravity.CENTER
        }

        tvScore = TextView(this).apply {
            text = "Puan: 0"
            textSize = 18f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            gravity = Gravity.CENTER
        }

        tvMoves = TextView(this).apply {
            text = "Hamle: 0"
            textSize = 18f
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            gravity = Gravity.CENTER
        }

        headerLayout.addView(tvScore)
        headerLayout.addView(tvMoves)
        headerCard.addView(headerLayout)
        mainLayout.addView(headerCard)

        // Grid for cards
        gridLayout = GridLayout(this).apply {
            columnCount = 4
            rowCount = 4
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        mainLayout.addView(gridLayout)

        // Restart button
        val btnRestart = Button(this).apply {
            text = "🔄 Yeni Oyun"
            textSize = 18f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 0)
            }
            setOnClickListener {
                resetGame()
            }
        }
        mainLayout.addView(btnRestart)

        setContentView(mainLayout)
    }

    private fun initializeGame() {
        // Create pairs of cards
        cards.clear()
        val allSymbols = symbols + symbols // Duplicate for pairs
        allSymbols.shuffled().forEachIndexed { index, symbol ->
            cards.add(MemoryCard(index, symbol))
        }

        // Create card views
        gridLayout.removeAllViews()
        cards.forEach { card ->
            val cardView = createCardView(card)
            gridLayout.addView(cardView)
        }
    }

    private fun createCardView(card: MemoryCard): CardView {
        val cardView = CardView(this).apply {
            val size = (resources.displayMetrics.widthPixels / 4) - 24
            layoutParams = GridLayout.LayoutParams().apply {
                width = size
                height = size
                setMargins(8, 8, 8, 8)
            }
            radius = 12f
            cardElevation = 4f
            setCardBackgroundColor(resources.getColor(R.color.colorPrimary, null))
        }

        val textView = TextView(this).apply {
            text = if (card.isFlipped) card.symbol else "?"
            textSize = 32f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        cardView.addView(textView)
        
        cardView.setOnClickListener {
            onCardClicked(card, textView, cardView)
        }

        card.view = textView
        card.cardView = cardView

        return cardView
    }

    private fun onCardClicked(card: MemoryCard, textView: TextView, cardView: CardView) {
        // Don't allow clicking if card is already flipped or matched
        if (card.isFlipped || card.isMatched || flippedCards.size >= 2) {
            return
        }

        // Flip the card
        card.isFlipped = true
        textView.text = card.symbol
        flippedCards.add(card.id)

        // Check for match if two cards are flipped
        if (flippedCards.size == 2) {
            moves++
            tvMoves.text = "Hamle: $moves"

            val firstCard = cards[flippedCards[0]]
            val secondCard = cards[flippedCards[1]]

            if (firstCard.symbol == secondCard.symbol) {
                // Match found!
                firstCard.isMatched = true
                secondCard.isMatched = true
                matchedPairs++
                score += 10
                tvScore.text = "Puan: $score"

                // Change card colors to indicate match
                Handler(Looper.getMainLooper()).postDelayed({
                    firstCard.cardView?.setCardBackgroundColor(Color.parseColor("#4CAF50"))
                    secondCard.cardView?.setCardBackgroundColor(Color.parseColor("#4CAF50"))
                    flippedCards.clear()

                    // Check if game is complete
                    if (matchedPairs == totalPairs) {
                        showGameCompleteDialog()
                    }
                }, 500)
            } else {
                // No match, flip cards back
                Handler(Looper.getMainLooper()).postDelayed({
                    firstCard.isFlipped = false
                    secondCard.isFlipped = false
                    firstCard.view?.text = "?"
                    secondCard.view?.text = "?"
                    flippedCards.clear()
                }, 1000)
            }
        }
    }

    private fun showGameCompleteDialog() {
        val message = when {
            moves <= 12 -> "🏆 Mükemmel! Harikasın!"
            moves <= 16 -> "⭐ Çok iyi! Başarılısın!"
            moves <= 20 -> "👍 İyi oynadın!"
            else -> "💪 Tebrikler! Tamamladın!"
        }

        AlertDialog.Builder(this)
            .setTitle("🎉 Oyun Bitti!")
            .setMessage("$message\n\nPuan: $score\nHamle: $moves")
            .setPositiveButton("Tekrar Oyna") { _, _ ->
                resetGame()
            }
            .setNegativeButton("Çıkış") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetGame() {
        matchedPairs = 0
        moves = 0
        score = 0
        flippedCards.clear()
        tvScore.text = "Puan: 0"
        tvMoves.text = "Hamle: 0"
        initializeGame()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class MemoryCard(
        val id: Int,
        val symbol: String,
        var isFlipped: Boolean = false,
        var isMatched: Boolean = false,
        var view: TextView? = null,
        var cardView: CardView? = null
    )
}
