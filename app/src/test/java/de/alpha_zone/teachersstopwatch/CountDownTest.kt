package de.alpha_zone.teachersstopwatch

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Assert.assertThat
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


@ExperimentalTime
class CountDownTest {

	@Test
	fun a_countdown_can_be_defined() {
		CountDown(20.toDuration(DurationUnit.SECONDS))
	}

	@Test
	fun on_start_count_down_has_start_time() {
		val countDown = CountDown(20.toDuration(DurationUnit.SECONDS))
		countDown.start()
		assertThat(countDown.start, `is`(not(nullValue())))
	}

	@Test
	fun on_start_count_down_has_correct_timers() {
		val cycles = 2
		val countDown = CountDown(20.toDuration(DurationUnit.SECONDS), cycles)
		countDown.start()
		val startTime = countDown.start!!
		assertThat(countDown.timers, containsInAnyOrder(startTime.plusSeconds(20L), startTime.plusSeconds(40L)))
	}

	@Test
	fun on_count_down_can_sync() {
		val cycles = 1
		val countDown = CountDown(20.toDuration(DurationUnit.SECONDS), cycles)
		countDown.start()
		val startTime = countDown.start!!
		countDown.sync(startTime.plusSeconds(1))
		assertThat(countDown.secondsPassed, `is`(1L))
	}

	@Test
	fun on_1_cycle_done_of_2_cycles_is_50_percent() {
		val cycles = 2
		val countDown = CountDown(1.toDuration(DurationUnit.SECONDS), cycles)
		countDown.start()
		val startTime = countDown.start!!
		countDown.sync(startTime.plusSeconds(1))
		assertThat(countDown.cyclesDone, `is`(50.0))
	}

	@Test
	fun on_1_second_done_of_2_second_cycle_is_50_percent() {
		val cycles = 2
		val countDown = CountDown(2.toDuration(DurationUnit.SECONDS), cycles)
		countDown.start()
		val startTime = countDown.start!!
		countDown.sync(startTime.plusSeconds(1))
		assertThat(countDown.cycleDone, `is`(50.0))
	}

	@Test
	fun second_1_cycle_with_interval_of_5_lies_in_cycle_0() {
		assertThat(1.toCycle(5), `is`(0.2))
	}

	@Test
	fun second_6_of_cycle_with_interval_of_5_lies_in_cycle_1() {
		assertThat(6.toCycle(5), `is`(1.2))
	}

	@Test
	fun second_3_of_cycle_with_interval_of_3_lies_is_3() {
		assertThat(3.toInnerCycle(3), `is`(3L))
	}
	@Test
	fun second_4_of_cycle_with_interval_of_3_lies_is_1() {
		assertThat(4.toInnerCycle(3), `is`(1L))
	}

}