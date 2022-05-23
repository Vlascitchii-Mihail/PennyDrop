@file:Suppress("unused")

package com.bignerdranch.android.geoquiz_2

import android.util.Log
import androidx.lifecycle.ViewModel
import android.content.Intent

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    init { Log.d(TAG, "ViewModel instance created")}

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var count = 0
    var isCheater = false
    private var answerIsTrue = false
    var data: Intent? = null
    var cheatTries = 0

    fun showAnswerText() : Int {
        return when {
        answerIsTrue -> R.string.true_button
        else -> R.string.false_button
        }
    }

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText : Int get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex < 0) 0
        else (currentIndex -1) % questionBank.size
    }
}