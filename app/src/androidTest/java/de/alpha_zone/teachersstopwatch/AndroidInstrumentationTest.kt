package de.alpha_zone.teachersstopwatch

import android.view.InputDevice
import android.view.MotionEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.actionWithAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.ExperimentalTime

@RunWith(AndroidJUnit4::class)
@ExperimentalTime
class AndroidInstrumentationTest {

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
        onView(withId(R.id.minutesInput)).check(matches(isDisplayed()))
    }

    @Test
    fun inputFieldForSecondsOfTimeoutCanBeUsed() {
        onView(withId(R.id.secondsInput)).check(matches(isDisplayed()))
    }

    @Test
    fun inputFieldForRepeatsCanBeUsed() {
        onView(withId(R.id.repeatInput)).check(matches(isDisplayed()))
    }

    @Test
    fun minValueOfSecondsIs0() {
        onNumberPickerInput(R.id.secondsInput)
            .check(matches(withText("0")))
        onNumberPicker(R.id.secondsInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.secondsInput)
            .check(matches(withText("59")))
    }

    @Test
    fun maxValueOfSecondsIs59() {
        onNumberPickerInput(R.id.secondsInput)
            .check(matches(withText("0")))
        onNumberPicker(R.id.secondsInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.secondsInput)
            .check(matches(withText("59")))
    }

    @Test
    fun minValueOfMinutesIs0() {
        onNumberPickerInput(R.id.minutesInput)
            .check(matches(withText("0")))
        onNumberPicker(R.id.minutesInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.minutesInput)
            .check(matches(withText("99")))
    }

    @Test
    fun maxValueOfMinutesIs99() {
        onNumberPickerInput(R.id.minutesInput)
            .check(matches(withText("0")))
        onNumberPicker(R.id.minutesInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.minutesInput)
            .check(matches(withText("99")))
    }

    @Test
    fun minValueOfRepeatsIs1() {
        onNumberPickerInput(R.id.repeatInput)
            .check(matches(withText("1")))
        onNumberPicker(R.id.repeatInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.repeatInput)
            .check(matches(withText("99")))
    }

    @Test
    fun maxValueOfRepeatsIs99() {
        onNumberPickerInput(R.id.repeatInput)
            .check(matches(withText("1")))
        onNumberPicker(R.id.repeatInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.repeatInput)
            .check(matches(withText("99")))
    }

    @Test
    fun start_button_can_be_pressed() {
        onView(withId(R.id.start)).perform(ViewActions.click())
    }

    @Test
    fun click_on_start_count_down_starts() {
        onNumberPicker(R.id.secondsInput)
            .perform(clickTopCentre)
        onNumberPickerInput(R.id.secondsInput)
            .check(matches(withText("59")))

        onView(withId(R.id.start_button)).check(matches(isDisplayed()))
        onView(withId(R.id.start_button)).perform(ViewActions.click())
        onView(withId(R.id.secondsInput)).check(matches(not(isEnabled())))
    }

    private fun onNumberPicker(id: Int): ViewInteraction {
        return onView(withId(id))
    }

    private fun onNumberPickerInput(id: Int): ViewInteraction {
        return onView(withParent(withId(id)))
    }

    private val clickTopCentre =
        actionWithAssertions(
            GeneralClickAction(
                Tap.SINGLE,
                GeneralLocation.TOP_CENTER,
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN,
                MotionEvent.BUTTON_PRIMARY
            )
        )
}