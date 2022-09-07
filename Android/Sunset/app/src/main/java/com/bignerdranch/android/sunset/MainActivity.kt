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

    //initialization the colors
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

        //starting the animation when user clicked on the screen
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

    //setting animation
    private fun startAnimation() {

        //start and finish positions of the sun
        /**
         * @param top, bottom, right, left - inner parameters of the View
         * @param hight - inner value - top minus bottom
         */
        val sunYStart = sunView.top.toFloat()
        val sunYEnd = skyView.height.toFloat()

        //creating the Animator to perform the animation - interpolation
        val heightAnimation = ObjectAnimator.ofFloat(sunView, "y", sunYStart, sunYEnd)
            .setDuration(3000)

        //AccelerateInterpolator() - smooth animation
        heightAnimation.interpolator = AccelerateInterpolator()

        //changing the sky's color
        val sunsetSkyAnimator = ObjectAnimator.ofInt(skyView, "backgroundColor", blueSkyColor,
        sunsetSkyColor).setDuration(3000)

        //setEvaluator(ArgbEvaluator()) - smooth color transition, announces transit colors between some colors
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        //changing the sky's color
        val nightSkyAnimator = ObjectAnimator.ofInt(skyView, "backgroundColor", sunsetSkyColor,
        nightSkyColor).setDuration(1500)

        //setEvaluator(ArgbEvaluator()) - smooth color transition, announces transit colors between some colors
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

//        heightAnimation.start()
//        sunsetSkyAnimator.start()

        //AnimatorSet() - animations set, setting the launching sequence
        val animatorSet = AnimatorSet()

        //playing the animation
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