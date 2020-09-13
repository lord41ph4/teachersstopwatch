package de.alpha_zone.teachersstopwatch

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class TimerBackendTest {

    @Test
    fun aCountDownCanBeDefined() {
        CountDown(20.toDuration(DurationUnit.SECONDS))
    }

    @Test
    fun onTickSecondsPassedIsIncreasedByOne() {
        val initialDuration = 20.toDuration(DurationUnit.SECONDS)
        val countDown = CountDown(initialDuration)
        countDown.onSecondPassed()
        assertEquals(countDown.secondsPassed, 1)
    }

    @Test
    fun onTickCountDownIsDecreasedByOneSecond() {
        val initialDuration = 20.toDuration(DurationUnit.SECONDS)
        val countDown = CountDown(initialDuration)
        countDown.onSecondPassed()
        assertEquals(countDown.computeTimeLeft(), 19.toDuration(DurationUnit.SECONDS))
    }

}