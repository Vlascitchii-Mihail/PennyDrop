package com.bignerdranch.android.geoquiz_2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.TextView
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton : Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button

    private val quizViewModel : QuizViewModel by lazy {
//        ViewModelProvider.AndroidViewModelFactory(application).create(QuizViewModel::class.java)
//        ViewModelProviders.of(this).get(QuizViewModel::class.java)
        ViewModelProvider(this)[QuizViewModel::class.java]
//        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(QuizViewModel::class.java)
    }
//    private val quizViewModel : QuizViewModel = QuizViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")

        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

//        val isCheater = savedInstanceState?.getBoolean(KEY_INDEX_CHEAT, false) ?: false
//        quizViewModel.isCheater = isCheater

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
////        val provider = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
////        val quizViewModel = provider.create(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewModel:$quizViewModel")

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }


        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data : Intent? = it.data
                quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false
            }
        }


        cheatButton.setOnClickListener {
//            val intent = Intent(this, CheatActivity2::class.java)
//            startActivity(intent)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity2.newIntent(this@MainActivity, answerIsTrue)
//            startActivity(intent)
//            startActivityForResult(intent, REQUEST_CODE_CHEAT)

            getContent.launch(intent)
        }

        updateQuestion()

    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != Activity.RESULT_OK) return
//        if (requestCode == REQUEST_CODE_CHEAT) {
//            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false
//
//        }
//    }



    private fun updateQuestion() {
//        Log.d(TAG, "Updating question text", Exception())
        trueButton.isEnabled = true
        falseButton.isEnabled = true
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
            val correctAnswer = quizViewModel.currentQuestionAnswer
//            val messageResId = if (userAnswer == correctAnswer) {
//                R.string.correct_toast
//            }
//                else R.string.incorrect_toast


        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
//        savedInstanceState.putBoolean(KEY_INDEX_CHEAT, quizViewModel.isCheater)
    }
}