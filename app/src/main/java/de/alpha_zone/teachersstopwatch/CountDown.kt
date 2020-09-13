package de.alpha_zone.teachersstopwatch

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CountDown(private val duration: Duration) {

    private var passedSeconds = 0L
    val secondsPassed: Long get() = passedSeconds

    fun onSecondPassed() {
        passedSeconds++
    }

    fun computeTimeLeft(): Duration {
        return duration.minus(passedSeconds.toDuration(DurationUnit.SECONDS))
    }

}
