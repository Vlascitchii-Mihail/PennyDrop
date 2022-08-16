package com.bignerdranch.android.geoquiz_2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import android.os.Build
import android.widget.Toast
import android.util.Log

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val SAVE_STATE = "com.bignerdranch.android.geoquiz.cheater_saved"
const val CHEAT_TRIES = "com.bignerdranch.android.geoquiz.cheat_tries"

class CheatActivity2 : AppCompatActivity() {

    private val cheatViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]

    }

    private var answerIsTrue = false
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var cheatValue = 1
    private lateinit var apiLevel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat2)

        cheatViewModel.isCheater = savedInstanceState?.getBoolean(SAVE_STATE, false) ?: false
        Log.d("CheatActivity2", "isCheater - ${cheatViewModel.isCheater}")
        cheatViewModel.cheatTries = savedInstanceState?.getInt(CHEAT_TRIES, 0) ?: 0
        Log.d("CheatActivity2", "saved ${cheatViewModel.cheatTries}")

//        answerIsTrue = savedInstanceState?.getBoolean(EXTRA_ANSWER_IS_TRUE) ?: false

        //reading data from intent from MainActivity, second argument - default value
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        cheatViewModel.cheatTries = intent.getIntExtra(CHEAT_TRIES, 0)

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevel = findViewById(R.id.api_level)

        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }

        if (cheatViewModel.cheatTries > 3) showAnswerButton.isEnabled = false

        showAnswerButton.setOnClickListener {
            answerTextView.setText(answerText)
            cheatViewModel.cheatTries++
            setAnswerShown()
            Log.d("CheatActivity2", "++ ${cheatViewModel.cheatTries}")


        }

        if (cheatViewModel.isCheater) {
            answerTextView.setText(answerText)
            setAnswerShown()
        }

        apiLevel.append(" ${Build.VERSION.SDK_INT}\nYou have ${if (3 - cheatViewModel.cheatTries <= 0) 0 else 3 - cheatViewModel.cheatTries} tries")
//        Toast.makeText(this, "You have ${if (3 - cheatViewModel.cheatTries <= 0) 0 else 3 - cheatViewModel.cheatTries} tries", Toast.LENGTH_LONG).show()
        Log.d("CheatActivity2", "${cheatViewModel.cheatTries}")
    }

    //creating a new intent, arguments from MainActivity
    companion object {
        //creating new intent for MainActivity
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, cheat: Int): Intent {
            //new intent
            return Intent(packageContext, CheatActivity2::class.java).apply {
                //puts data in intent
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(CHEAT_TRIES, cheat)
            }
        }

        fun getBooleanIsCheater(intent: Intent) = intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)?: false


    }

    private fun setAnswerShown() {
        cheatViewModel.isCheater = true
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isCheater)
            putExtra(cheat, cheatValue)
            putExtra(CHEAT_TRIES, cheatViewModel.cheatTries)
        }
        //setResult() - sends the intent to the MainActivity
        setResult(Activity.RESULT_OK, data)
        showAnswerButton.isEnabled = false
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putBoolean(SAVE_STATE, cheatViewModel.isCheater)
        savedInstanceState.putInt(CHEAT_TRIES, cheatViewModel.cheatTries)
        Log.d("CheatActivity2", "target ${cheatViewModel.cheatTries}")


    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("CheatActivity2", "Cheat onDestroy()")
    }



//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d(NAME, "onDestroy() is called")
//        cheatViewModel.isCheater = false
//    }
}