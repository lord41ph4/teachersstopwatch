package de.alpha_zone.teachersstopwatch

import android.content.Context
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CountDownSchedulerTest {
	@Test
	fun countdown_can_be_created() {
		CountDownScheduler(CountDown(20.toDuration(DurationUnit.SECONDS)))
	}

	@Test
	fun after_countdown_is_started_scheduling_can_be_done() {
		val countDown = CountDown(20.toDuration(DurationUnit.SECONDS), 3)
		countDown.start()
		val scheduler = CountDownScheduler(countDown)
		scheduler.schedule(mock(Context::class.java))
		assertThat(scheduler.hasScheduledTasks, `is`(true))
	}

	@Test
	fun after_countdown_is_started_scheduled_tasks_can_be_cancelled() {
		val countDown = CountDown(20.toDuration(DurationUnit.SECONDS), 3)
		countDown.start()
		val scheduler = CountDownScheduler(countDown)
		scheduler.schedule(mock(Context::class.java))
		scheduler.cancel(mock(Context::class.java))
		assertThat(scheduler.hasScheduledTasks, `is`(false))
	}

}
