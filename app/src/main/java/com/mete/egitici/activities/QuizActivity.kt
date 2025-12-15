package com.mete.egitici.activities

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mete.egitici.R
import org.json.JSONObject

class QuizActivity : AppCompatActivity() {
    
    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var questions: List<QuestionData>
    private lateinit var questionTextView: TextView
    private lateinit var optionsLayout: LinearLayout
    private lateinit var scoreTextView: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupToolbar()
        loadQuestions()
        createUI()
        showQuestion()
    }
    
    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "🎯 Quiz Yarışması"
    }
    
    private fun createUI() {
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(32, 32, 32, 32)
        }
        
        scoreTextView = TextView(this).apply {
            text = "Puan: $score / ${questions.size}"
            textSize = 18f
            setTextColor(resources.getColor(R.color.colorPrimary, null))
            setPadding(0, 0, 0, 24)
        }
        mainLayout.addView(scoreTextView)
        
        questionTextView = TextView(this).apply {
            text = ""
            textSize = 20f
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 32)
        }
        mainLayout.addView(questionTextView)
        
        optionsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        mainLayout.addView(optionsLayout)
        
        val scrollView = android.widget.ScrollView(this).apply {
            addView(mainLayout)
        }
        
        setContentView(scrollView)
    }
    
    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResults()
            return
        }
        
        val question = questions[currentQuestionIndex]
        questionTextView.text = "${currentQuestionIndex + 1}. ${question.question}"
        
        optionsLayout.removeAllViews()
        
        question.options.forEachIndexed { index, option ->
            val button = Button(this).apply {
                text = option
                textSize = 16f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 16)
                }
                setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                setTextColor(Color.WHITE)
                
                setOnClickListener {
                    checkAnswer(index, question.correctAnswer)
                }
            }
            optionsLayout.addView(button)
        }
    }
    
    private fun checkAnswer(selectedAnswer: Int, correctAnswer: Int) {
        if (selectedAnswer == correctAnswer) {
            score++
            Toast.makeText(this, "✅ Doğru! Harika!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "❌ Yanlış. Doğru cevap: ${questions[currentQuestionIndex].options[correctAnswer]}", Toast.LENGTH_SHORT).show()
        }
        
        scoreTextView.text = "Puan: $score / ${questions.size}"
        
        // Move to next question
        currentQuestionIndex++
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            showQuestion()
        }, 1500)
    }
    
    private fun showResults() {
        questionTextView.text = "🎉 Quiz Tamamlandı!"
        optionsLayout.removeAllViews()
        
        val resultText = TextView(this).apply {
            text = "Toplam Puan: $score / ${questions.size}\n\n" +
                   "Başarı Oranı: ${(score * 100 / questions.size)}%\n\n" +
                   when {
                       score == questions.size -> "🏆 Mükemmel! Hepsini doğru bildin!"
                       score >= questions.size * 0.7 -> "⭐ Çok iyi! Harikasın!"
                       score >= questions.size * 0.5 -> "👍 İyi! Daha da iyileşebilirsin!"
                       else -> "💪 Çalışmaya devam et!"
                   }
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        optionsLayout.addView(resultText)
        
        val restartButton = Button(this).apply {
            text = "Tekrar Oyna"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 32, 0, 0)
            }
            setOnClickListener {
                currentQuestionIndex = 0
                score = 0
                scoreTextView.text = "Puan: 0 / ${questions.size}"
                showQuestion()
            }
        }
        optionsLayout.addView(restartButton)
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
