package com.bignerdranch.android.pennydrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//point of entry in our app
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}