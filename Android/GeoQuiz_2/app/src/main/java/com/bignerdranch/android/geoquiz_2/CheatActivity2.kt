package com.bignerdranch.android.geoquiz_2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import android.util.Log

private const val NAME = "CheatActivity2"
private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val SAVE_STATE = "com.bignerdranch.android.geoquiz.cheater_saved"

class CheatActivity2 : AppCompatActivity() {

    private val cheatViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]

    }

    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat2)

        cheatViewModel.isCheater = savedInstanceState?.getBoolean(SAVE_STATE, false) ?: false

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }

        showAnswerButton.setOnClickListener {
            answerTextView.setText(answerText)
            setAnswerShown(true)
        }

        if (cheatViewModel.isCheater) {
            answerTextView.setText(answerText)
            setAnswerShown(true)
        }

    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity2::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    private fun setAnswerShown(isAnswerShown: Boolean) {
        cheatViewModel.isCheater = true
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(SAVE_STATE, cheatViewModel.isCheater)

    }

//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(NAME, "onDestroy() is called")
//        cheatViewModel.isCheater = false
//    }
}