package de.alpha_zone.teachersstopwatch

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CountDown(duration: Duration, private val cycles: Int = 1) {

	private val interval = duration.inSeconds.toLong()
	private var _start: LocalDateTime? = null
	val start: LocalDateTime? get() = _start

	private val _times: MutableList<LocalDateTime> = mutableListOf()
	val timers: List<LocalDateTime> get() = _times
	val unfinishedTimers: List<LocalDateTime> get() = _times.filter { it.isAfter(LocalDateTime.now()) }

	private var _secondsPassed = 0L
	val secondsPassed: Long get() = _secondsPassed
	private var _secondsToFinish = 0L
	val secondsToFinish: Long get() = _secondsToFinish
	private var _finished = true
	val finished: Boolean get() = _finished
	val notFinished: Boolean get() = !finished;

	val cyclesDone: Double get() = secondsPassed.toCycleRelative(interval, cycles)
	val cycleDone: Double get() = secondsPassed.toInnerCycleRelative(interval)

	private var _syncCount = 0L
	val syncCount: Long get() = _syncCount

	fun start() {
		val now = LocalDateTime.now()
		_start = now
		for (i in 1..cycles) {
			_times.add(now.plusSeconds(interval * i))
		}
	}

	fun sync(syncTime: LocalDateTime = LocalDateTime.now()) {
		_syncCount++
		val start = _start
		if (start != null) {
			_secondsPassed = (start.until(syncTime, ChronoUnit.MILLIS).toDouble() / 1000).roundToLong()
			_secondsToFinish = (interval * cycles) - _secondsPassed
			_finished = secondsToFinish <= 0;
		}
	}
}

fun Number.toCycle(size: Long): Double {
	return (this.toDouble() / size)
}

fun Number.toCycleRelative(size: Long, cycles: Int): Double {
	return (this.toCycle(size) / cycles) * 100
}

fun Number.toInnerCycle(size: Long): Long {
	val part = this.toLong() % size
	return when {
		this.toLong() <= 0L -> {
			0
		}
		part == 0L -> {
			size
		}
		else -> {
			part
		}
	}
}

fun Number.toInnerCycleRelative(size: Long): Double {
	return (this.toInnerCycle(size).toDouble() / size) * 100
}