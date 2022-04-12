package com.bignerdranch.android.geoquiz
//Hello from Git

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private  var trueAnswer = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton:ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")

        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)


        trueButton.setOnClickListener{
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            checkAnswer(true)
        }


        falseButton.setOnClickListener{
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            checkAnswer(false)
        }


        questionTextView.setOnClickListener{ updateQuestion() }


        questionTextView.setText(quizViewModel.questionBank[0].textResId)


        nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            if (quizViewModel.currentIndex == quizViewModel.questionBank.size - 1) nextButton.isEnabled = false
        }


        prevButton.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex - 1) % quizViewModel.questionBank.size
            if (quizViewModel.currentIndex < 1) quizViewModel.currentIndex = 0
            questionTextView.setText(quizViewModel.questionBank[quizViewModel.currentIndex].textResId)
            if (quizViewModel.currentIndex < (quizViewModel.questionBank.size - 1)) nextButton.isEnabled = true

        }
    }


    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }


    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer)
            R.string.correct_toast
        else R.string.incorrect_toast

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        if (messageResId == R.string.correct_toast) userWinRate()
    }


    private fun userWinRate() {
        ++trueAnswer
        if (quizViewModel.currentIndex == (quizViewModel.questionBank.size - 1) &&
            (!trueButton.isEnabled || !falseButton.isEnabled)) {

            Toast.makeText(this,
                "Win rate is ${trueAnswer * 100 / quizViewModel.questionBank.size}" +
                        " percents", Toast.LENGTH_LONG).show()
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }


    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }


    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }



    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}
