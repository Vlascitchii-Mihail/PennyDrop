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
import androidx.core.app.ActivityOptionsCompat
import android.os.Build

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
const val cheat = "CheatActivity2"

//AppCompatActivity - это подкласс, наследующий от класса Android Activity и
//обеспечивающий поддержку старых версий Android
class MainActivity : AppCompatActivity() {

    private lateinit var trueButton : Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private var cheatValue = 0


    private val quizViewModel : QuizViewModel by lazy {
//        ViewModelProvider.AndroidViewModelFactory(application).create(QuizViewModel::class.java)
//        ViewModelProviders.of(this).get(QuizViewModel::class.java)
        ViewModelProvider(this)[QuizViewModel::class.java]
//        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(QuizViewModel::class.java)
    }
//    private val quizViewModel : QuizViewModel = QuizViewModel()

//вызывается при создании экземпляра подкласса activity.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")

        //заполняет макет и выводит его на экран
    //R.layout.activity_main - идентификатор ресурса
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex
        cheatValue = savedInstanceState?.getInt(cheat, 0) ?: 0
        quizViewModel.cheatTries += savedInstanceState?.getInt(CHEAT_TRIES, 0) ?: 0

//        val isCheater = savedInstanceState?.getBoolean(KEY_INDEX_CHEAT, false) ?: false
//        quizViewModel.isCheater = isCheater

//        val provider: ViewModelProvider = ViewModelProviders.of(this)
//        val quizViewModel = provider.get(QuizViewModel::class.java)
////        val provider = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
////        val quizViewModel = provider.create(QuizViewModel::class.java)
//        Log.d(TAG, "Got a QuizViewModel:$quizViewModel")

    //getting reference on widgets
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

    //filling interface  View.OnClickListener and creating listeners
        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            quizViewModel.isCheater = false
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
                cheatValue += data?.getIntExtra(cheat, 0) ?: 0
                quizViewModel.cheatTries = data?.getIntExtra(CHEAT_TRIES, 0) ?: 0
                Toast.makeText(this, "You cheated $cheatValue times", Toast.LENGTH_SHORT).show()

                Log.d(TAG, "CheatTries = ${quizViewModel.cheatTries}")


            }
        }


        cheatButton.setOnClickListener {
//            val intent = Intent(this, CheatActivity2::class.java)
//            startActivity(intent)
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity2.newIntent(this@MainActivity, answerIsTrue, quizViewModel.cheatTries)
//            startActivity(intent)
//            startActivityForResult(intent, REQUEST_CODE_CHEAT)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options =
                    ActivityOptionsCompat.makeClipRevealAnimation(it, 0, 0, it.width, it.height)

                getContent.launch(intent, options)
            } else {
                getContent.launch(intent)
            }

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

//обновляет вопрос
    private fun updateQuestion() {
//        Log.d(TAG, "Updating question text", Exception())
        trueButton.isEnabled = true
        falseButton.isEnabled = true
            val questionTextResId = quizViewModel.currentQuestionText

        //setting the text in questionTextView
            questionTextView.setText(questionTextResId)
    }

    //проверка ответа пользователя
    private fun checkAnswer(userAnswer: Boolean) {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
            val correctAnswer = quizViewModel.currentQuestionAnswer
//            val messageResId = if (userAnswer == correctAnswer) {
//                R.string.correct_toast
//            }
//                else R.string.incorrect_toast


        val messageResId = when {
            quizViewModel.isCheater ->  R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        //this: Context - экземпляр MainActivity
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
        savedInstanceState.putInt(cheat, cheatValue)
        savedInstanceState.putInt(CHEAT_TRIES, quizViewModel.cheatTries)
//        savedInstanceState.putBoolean(KEY_INDEX_CHEAT, quizViewModel.isCheater)
    }
}