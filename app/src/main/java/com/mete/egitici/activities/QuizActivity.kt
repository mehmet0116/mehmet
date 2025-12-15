package com.mete.egitici.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mete.egitici.R
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {
    
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var questions: List<QuestionData>
    
    // UI Elements
    private lateinit var tvScore: TextView
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var optionsLayout: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var feedbackLayout: LinearLayout
    private lateinit var tvFeedback: TextView
    private lateinit var resultsLayout: LinearLayout
    private lateinit var tvFinalScore: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvResultMessage: TextView
    private lateinit var btnRestart: Button
    private lateinit var btnHome: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        setupToolbar()
        loadQuestions()
        initializeViews()
        showQuestion()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🎯 Quiz Yarışması"
    }
    
    private fun initializeViews() {
        tvScore = findViewById(R.id.tvScore)
        tvQuestionCount = findViewById(R.id.tvQuestionCount)
        tvCategory = findViewById(R.id.tvCategory)
        tvQuestion = findViewById(R.id.tvQuestion)
        optionsLayout = findViewById(R.id.optionsLayout)
        progressBar = findViewById(R.id.progressBar)
        feedbackLayout = findViewById(R.id.feedbackLayout)
        tvFeedback = findViewById(R.id.tvFeedback)
        resultsLayout = findViewById(R.id.resultsLayout)
        tvFinalScore = findViewById(R.id.tvFinalScore)
        tvPercentage = findViewById(R.id.tvPercentage)
        tvResultMessage = findViewById(R.id.tvResultMessage)
        btnRestart = findViewById(R.id.btnRestart)
        btnHome = findViewById(R.id.btnHome)
        
        btnRestart.setOnClickListener {
            restartQuiz()
        }
        
        btnHome.setOnClickListener {
            finish()
        }
        
        updateScore()
    }
    
    private fun updateScore() {
        tvScore.text = "⭐ Puan: $score"
        tvQuestionCount.text = "📝 Soru: ${currentQuestionIndex + 1}/${questions.size}"
        
        // Update progress bar
        val progress = ((currentQuestionIndex.toFloat() / questions.size) * 100).toInt()
        progressBar.progress = progress
    }
    
    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResults()
            return
        }
        
        val question = questions[currentQuestionIndex]
        
        // Update UI
        updateScore()
        tvQuestion.text = question.question
        
        // Show category with emoji
        val categoryEmoji = when(question.category) {
            "math" -> "🔢"
            "language" -> "📚"
            "science" -> "🔬"
            "cognitive" -> "🧠"
            else -> "📖"
        }
        val categoryName = when(question.category) {
            "math" -> "Matematik"
            "language" -> "Dil"
            "science" -> "Fen Bilgisi"
            "cognitive" -> "Bilişsel"
            else -> "Genel"
        }
        tvCategory.text = "$categoryEmoji $categoryName"
        
        // Hide feedback and results
        feedbackLayout.visibility = View.GONE
        resultsLayout.visibility = View.GONE
        
        // Clear and populate options
        optionsLayout.removeAllViews()
        
        question.options.forEachIndexed { index, option ->
            val optionCard = createOptionButton(option, index, question.correctAnswer)
            optionsLayout.addView(optionCard)
        }
    }
    
    private fun createOptionButton(text: String, index: Int, correctAnswer: Int): CardView {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 12f
            cardElevation = 4f
            
            val button = Button(this@QuizActivity).apply {
                text = this@apply.text
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(24, 24, 24, 24)
                setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                setTextColor(Color.WHITE)
                isAllCaps = false
                
                setOnClickListener {
                    checkAnswer(index, correctAnswer)
                }
            }
            
            addView(button)
        }
    }
    
    private fun checkAnswer(selectedAnswer: Int, correctAnswer: Int) {
        // Disable all buttons to prevent multiple clicks
        for (i in 0 until optionsLayout.childCount) {
            val cardView = optionsLayout.getChildAt(i) as? CardView
            cardView?.getChildAt(0)?.isEnabled = false
        }
        
        val isCorrect = selectedAnswer == correctAnswer
        
        // Show feedback
        feedbackLayout.visibility = View.VISIBLE
        if (isCorrect) {
            score++
            tvFeedback.text = "✅ Harika! Doğru cevap!"
            tvFeedback.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            val correctOption = questions[currentQuestionIndex].options[correctAnswer]
            tvFeedback.text = "❌ Yanlış! Doğru cevap: $correctOption"
            tvFeedback.setTextColor(Color.parseColor("#F44336"))
        }
        
        // Update score display
        updateScore()
        
        // Move to next question after delay
        currentQuestionIndex++
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            showQuestion()
        }, 2000)
    }
    
    private fun showResults() {
        // Hide question UI
        tvQuestion.visibility = View.GONE
        tvCategory.visibility = View.GONE
        optionsLayout.visibility = View.GONE
        feedbackLayout.visibility = View.GONE
        
        // Show results
        resultsLayout.visibility = View.VISIBLE
        
        val percentage = (score * 100) / questions.size
        
        tvFinalScore.text = "Toplam Puan: $score / ${questions.size}"
        tvPercentage.text = "Başarı Oranı: $percentage%"
        
        val (message, emoji) = when {
            score == questions.size -> "Mükemmel! Hepsini doğru bildin!" to "🏆"
            percentage >= 80 -> "Harika! Çok başarılısın!" to "⭐"
            percentage >= 60 -> "İyi! Başarılısın!" to "👍"
            percentage >= 40 -> "Fena değil! Biraz daha çalışmalısın." to "💪"
            else -> "Çalışmaya devam et! Başarırsın!" to "📚"
        }
        
        tvResultMessage.text = "$emoji $message"
        
        // Update progress to 100%
        progressBar.progress = 100
    }
    
    private fun restartQuiz() {
        // Reset quiz state
        currentQuestionIndex = 0
        score = 0
        
        // Show question UI
        tvQuestion.visibility = View.VISIBLE
        tvCategory.visibility = View.VISIBLE
        optionsLayout.visibility = View.VISIBLE
        resultsLayout.visibility = View.GONE
        feedbackLayout.visibility = View.GONE
        
        // Reset progress
        progressBar.progress = 0
        
        // Show first question
        showQuestion()
    }
    
    private fun loadQuestions() {
        val questionsList = mutableListOf<QuestionData>()
        try {
            val jsonString = assets.open("data/questions.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("questions")
            
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val optionsArray = item.getJSONArray("options")
                val options = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    options.add(optionsArray.getString(j))
                }
                
                questionsList.add(QuestionData(
                    id = item.getInt("id"),
                    question = item.getString("question"),
                    options = options,
                    correctAnswer = item.getInt("correctAnswer"),
                    category = item.getString("category")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        questions = questionsList
    }
    
    data class QuestionData(
        val id: Int,
        val question: String,
        val options: List<String>,
        val correctAnswer: Int,
        val category: String
    )
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
