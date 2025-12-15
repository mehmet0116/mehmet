package com.mete.egitici.activities

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mete.egitici.R
import kotlin.random.Random

class MathQuizActivity : AppCompatActivity() {

    private var currentQuestionIndex = 0
    private var score = 0
    private var correctAnswersInARow = 0
    private val totalQuestions = 15
    private lateinit var questions: MutableList<MathQuestion>
    
    // UI Elements
    private lateinit var tvScore: TextView
    private lateinit var tvQuestionCount: TextView
    private lateinit var tvTimer: TextView
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
    
    private var timer: CountDownTimer? = null
    private val timePerQuestion = 30000L // 30 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        
        setupToolbar()
        generateQuestions()
        initializeViews()
        showQuestion()
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🔢 Matematik Quiz"
    }

    private fun initializeViews() {
        tvScore = findViewById(R.id.tvScore)
        tvQuestionCount = findViewById(R.id.tvQuestionCount)
        tvTimer = findViewById(R.id.tvTimer)
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
        
        // Show timer for math quiz
        tvTimer.visibility = View.VISIBLE
        
        btnRestart.setOnClickListener {
            restartQuiz()
        }
        
        btnHome.setOnClickListener {
            finish()
        }
        
        updateScore()
    }

    private fun generateQuestions() {
        questions = mutableListOf()
        
        // Generate random math questions
        repeat(totalQuestions) {
            val difficulty = when {
                it < 5 -> "easy"
                it < 10 -> "medium"
                else -> "hard"
            }
            questions.add(generateRandomQuestion(difficulty))
        }
    }

    private fun generateRandomQuestion(difficulty: String): MathQuestion {
        return when (difficulty) {
            "easy" -> {
                val num1 = Random.nextInt(1, 11)
                val num2 = Random.nextInt(1, 11)
                val operation = if (Random.nextBoolean()) "+" else "-"
                
                if (operation == "+") {
                    val answer = num1 + num2
                    MathQuestion(
                        "$num1 + $num2 = ?",
                        generateOptions(answer),
                        answer
                    )
                } else {
                    val larger = maxOf(num1, num2)
                    val smaller = minOf(num1, num2)
                    val answer = larger - smaller
                    MathQuestion(
                        "$larger - $smaller = ?",
                        generateOptions(answer),
                        answer
                    )
                }
            }
            "medium" -> {
                val num1 = Random.nextInt(10, 21)
                val num2 = Random.nextInt(1, 11)
                val operations = listOf("+", "-", "×")
                val operation = operations.random()
                
                when (operation) {
                    "+" -> {
                        val answer = num1 + num2
                        MathQuestion("$num1 + $num2 = ?", generateOptions(answer), answer)
                    }
                    "-" -> {
                        val answer = num1 - num2
                        MathQuestion("$num1 - $num2 = ?", generateOptions(answer), answer)
                    }
                    else -> { // ×
                        val answer = num1 * num2
                        MathQuestion("$num1 × $num2 = ?", generateOptions(answer), answer)
                    }
                }
            }
            else -> { // hard
                val num1 = Random.nextInt(5, 13)
                val num2 = Random.nextInt(2, 11)
                val answer = num1 * num2
                MathQuestion("$num1 × $num2 = ?", generateOptions(answer), answer)
            }
        }
    }

    private fun generateOptions(correctAnswer: Int): List<String> {
        val options = mutableSetOf(correctAnswer.toString())
        
        while (options.size < 4) {
            val offset = Random.nextInt(-5, 6)
            val wrongAnswer = correctAnswer + offset
            if (wrongAnswer > 0 && wrongAnswer != correctAnswer) {
                options.add(wrongAnswer.toString())
            }
        }
        
        return options.shuffled()
    }

    private fun updateScore() {
        tvScore.text = "⭐ Puan: $score"
        tvQuestionCount.text = "📝 ${currentQuestionIndex + 1}/$totalQuestions"
        
        val progress = ((currentQuestionIndex.toFloat() / totalQuestions) * 100).toInt()
        progressBar.progress = progress
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResults()
            return
        }
        
        val question = questions[currentQuestionIndex]
        
        updateScore()
        tvQuestion.text = question.question
        
        feedbackLayout.visibility = View.GONE
        resultsLayout.visibility = View.GONE
        
        optionsLayout.removeAllViews()
        
        val correctAnswerIndex = question.options.indexOf(question.answer.toString())
        question.options.forEachIndexed { index, option ->
            val optionCard = createOptionButton(option, index, correctAnswerIndex)
            optionsLayout.addView(optionCard)
        }
        
        startTimer()
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timePerQuestion, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                tvTimer.text = "⏱️ ${seconds}s"
                
                // Change color when time is running out
                if (seconds <= 10) {
                    tvTimer.setTextColor(Color.RED)
                } else {
                    tvTimer.setTextColor(Color.WHITE)
                }
            }

            override fun onFinish() {
                tvTimer.text = "⏱️ 0s"
                checkAnswer(-1, question.options.indexOf(questions[currentQuestionIndex].answer.toString()))
            }
        }.start()
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
            
            val button = Button(this@MathQuizActivity).apply {
                this.text = text
                textSize = 20f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(24, 32, 24, 32)
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
        timer?.cancel()
        
        // Disable all buttons
        for (i in 0 until optionsLayout.childCount) {
            val cardView = optionsLayout.getChildAt(i) as? CardView
            cardView?.getChildAt(0)?.isEnabled = false
        }
        
        val isCorrect = selectedAnswer == correctAnswer
        
        feedbackLayout.visibility = View.VISIBLE
        if (isCorrect && selectedAnswer != -1) {
            correctAnswersInARow++
            val bonus = if (correctAnswersInARow >= 3) 5 else 0
            score += 10 + bonus
            
            val feedbackMsg = if (bonus > 0) {
                "✅ Harika! +$bonus bonus puan! 🔥"
            } else {
                "✅ Doğru! Harika!"
            }
            tvFeedback.text = feedbackMsg
            tvFeedback.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            correctAnswersInARow = 0
            val correctOption = questions[currentQuestionIndex].answer
            val msg = if (selectedAnswer == -1) {
                "⏰ Süre doldu! Doğru cevap: $correctOption"
            } else {
                "❌ Yanlış! Doğru cevap: $correctOption"
            }
            tvFeedback.text = msg
            tvFeedback.setTextColor(Color.parseColor("#F44336"))
        }
        
        updateScore()
        
        currentQuestionIndex++
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            showQuestion()
        }, 2000)
    }

    private fun showResults() {
        timer?.cancel()
        
        tvQuestion.visibility = View.GONE
        optionsLayout.visibility = View.GONE
        feedbackLayout.visibility = View.GONE
        tvTimer.visibility = View.GONE
        
        resultsLayout.visibility = View.VISIBLE
        
        val percentage = (score * 100) / (totalQuestions * 10)
        
        tvFinalScore.text = "Toplam Puan: $score / ${totalQuestions * 10}"
        tvPercentage.text = "Başarı Oranı: $percentage%"
        
        val (message, emoji) = when {
            percentage >= 90 -> "Matematik dehası! 🧮" to "🏆"
            percentage >= 75 -> "Harika! Çok başarılısın!" to "⭐"
            percentage >= 60 -> "İyi! İlerleyebilirsin!" to "👍"
            percentage >= 40 -> "Fena değil! Biraz daha pratik yap." to "💪"
            else -> "Çalışmaya devam et!" to "📚"
        }
        
        tvResultMessage.text = "$emoji $message"
        progressBar.progress = 100
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        correctAnswersInARow = 0
        
        generateQuestions()
        
        tvQuestion.visibility = View.VISIBLE
        optionsLayout.visibility = View.VISIBLE
        resultsLayout.visibility = View.GONE
        feedbackLayout.visibility = View.GONE
        tvTimer.visibility = View.VISIBLE
        
        progressBar.progress = 0
        showQuestion()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    data class MathQuestion(
        val question: String,
        val options: List<String>,
        val answer: Int
    )
}
