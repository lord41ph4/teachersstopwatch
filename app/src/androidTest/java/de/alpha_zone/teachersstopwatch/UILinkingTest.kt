package de.alpha_zone.teachersstopwatch

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UILinkingTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("de.alpha_zone.teachersstopwatch", appContext.packageName)
    }

    @Test
    fun inputFieldForMinutesOfTimeoutCanBeUsed() {
        onView(withId(R.id.minutesInput)).perform(swipeDown())
    }

    @Test
    fun inputFieldForSecondsOfTimeoutCanBeUsed() {
        onView(withId(R.id.secondsInput)).perform(swipeDown())
    }

    @Test
    fun inputFieldForRepeatsCanBeUsed() {
        onView(withId(R.id.repeatInput)).perform(swipeUp())
    }

    @Test
    fun startButtonCanBePressed() {
        onView(withId(R.id.start)).perform(click())
    }
}