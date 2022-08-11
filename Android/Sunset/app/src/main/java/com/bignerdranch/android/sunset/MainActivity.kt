package com.bignerdranch.android.sunset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.animation.ObjectAnimator
import android.view.animation.AccelerateInterpolator
import androidx.core.content.ContextCompat
import android.animation.ArgbEvaluator
import android.animation.AnimatorSet

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: View
    private lateinit var sunView: View
    private lateinit var skyView: View
    private var sunPositionUp = true

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }
    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }
    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.scene)
        sunView = findViewById(R.id.sun)
        skyView = findViewById(R.id.sky)

        sceneView.setOnClickListener {
//            reverseAnimation()
            sunPositionUp = if (sunPositionUp) {
                startAnimation()
                false
            } else {
                reverseAnimation()
                true
            }
        }
    }

    private fun startAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()

        val heightAnimation = ObjectAnimator.ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimation.interpolator = AccelerateInterpolator()

        val sunsetSkyAnimator = ObjectAnimator.ofInt(skyView, "backgroundColor", blueSkyColor,
        sunsetSkyColor).setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator.ofInt(skyView, "backgroundColor", sunsetSkyColor,
        nightSkyColor).setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

//        heightAnimation.start()
//        sunsetSkyAnimator.start()

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimation).with(sunsetSkyAnimator).before(nightSkyAnimator)
        animatorSet.start()
    }

    private fun reverseAnimation() {
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()

        val sunRise = ObjectAnimator.ofFloat(sunView, "y", sunYEnd, sunYStart)
            .setDuration(3000)
        sunRise.interpolator = AccelerateInterpolator()

        val sunRiseSkyColor = ObjectAnimator.ofInt(skyView, "backgroundColor", sunsetSkyColor,
        blueSkyColor).setDuration(3000)
        sunRiseSkyColor.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator.ofInt(skyView, "backgroundColor", nightSkyColor,
        sunsetSkyColor).setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val reverseAnimatorSet = AnimatorSet()
        reverseAnimatorSet.play(nightSkyAnimator).before(sunRiseSkyColor).with(sunRise)
        reverseAnimatorSet.start()
    }
}