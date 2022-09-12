package com.bignerdranch.android.beatbox

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@RunWith - shows that it's a Android test, which works with runTime resources
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    //@get:Rule - shows that it's required to start the MainActivity
//    @get:Rule
//    val activityRule = ActivityTestRule(MainActivity::class.java)


    //checking first button's name
    @Test
    fun showFirstFileName() {
        onView(withText("65_cjipie")).check(matches(isDisplayed()))
        onView(withText("65_cjipie")).perform(click())
    }
}