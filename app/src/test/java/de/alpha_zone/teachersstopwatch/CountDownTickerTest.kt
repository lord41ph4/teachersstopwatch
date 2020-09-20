package de.alpha_zone.teachersstopwatch;

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.ProgressBar
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.anyLong
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.ArrayDeque
import java.util.Deque
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CountDownTickerTest {

	@Test
	fun countdown_is_called_by_handler_until_done() {
		val (queue, mockHandler) = createHandlerMock()
		val countdown = CountDown(2.toDuration(DurationUnit.SECONDS))
		val countdownTicker = CountDownTicker(mockHandler, countdown)
		assertThat(countdown.syncCount, `is`(0))
		countdownTicker.start()
		emulateHandlerWork(queue)
		assertThat(countdown.secondsToFinish, `is`(0))
	}

	@Test
	fun when_count_down_is_on_timer_broadcast_is_fired() {
		val (queue, mockHandler) = createHandlerMock()
		val countdown = CountDown(2.toDuration(DurationUnit.SECONDS))
		val context = mock(Context::class.java)
		val intentReference: AtomicReference<Intent> = AtomicReference()
		`when`(context.sendBroadcast(any(Intent::class.java))).thenAnswer { invocation ->
			val argument = invocation.getArgumentAt(0, Intent::class.java)
			intentReference.set(argument)
			null
		}
		val countdownTicker = CountDownTicker(mockHandler, countdown, context)
		countdownTicker.start()
		emulateHandlerWork(queue)
		val intent = intentReference.get()
		assertThat(intent, `is`(not(nullValue())))
		assertThat(intent.getIntExtra(CountDownScheduler.NOTIFICATION_NAME, -1), `is`(not(-1)))
	}

	@Test
	fun countdown_can_be_cancelled() {
		val (queue, mockHandler) = createHandlerMock()
		val countdown = CountDown(10.toDuration(DurationUnit.SECONDS))
		val countdownTicker = CountDownTicker(mockHandler, countdown)
		countdownTicker.start()
		countdownTicker.cancel()
		val syncCount = countdown.syncCount
		emulateHandlerWork(queue)
		assertThat(countdown.syncCount, `is`(syncCount))
		assertThat(countdown.secondsToFinish, `is`(not(0L)))
	}

	@Test
	fun countdown_can_handle_ticker_process_bar() {
		val (queue, mockHandler) = createHandlerMock()
		val countdown = CountDown(1.toDuration(DurationUnit.SECONDS))
		val (progress, tickBar) = createProgressBarMock()

		val countdownTicker = CountDownTicker(mockHandler, countdown)
				.withTickBar(tickBar)
		countdownTicker.start()
		emulateHandlerWork(queue)
		assertThat(progress.get(), `is`(100))
	}

	@Test
	fun countdown_can_handle_repeat_process_bar() {
		val (queue, mockHandler) = createHandlerMock()
		val countdown = CountDown(1.toDuration(DurationUnit.SECONDS))
		val (progress, repeatBar) = createProgressBarMock()

		val countdownTicker = CountDownTicker(mockHandler, countdown)
				.withRepeatBar(repeatBar)
		countdownTicker.start()
		emulateHandlerWork(queue)
		assertThat(progress.get(), `is`(100))
	}

	private fun emulateHandlerWork(queue: Deque<Runnable>) {
		var task: Runnable? = queue.poll()
		while (task != null) {
			task.run()
			task = queue.poll()
		}
	}

	private fun createHandlerMock(): Pair<Deque<Runnable>, Handler> {
		val queue: Deque<Runnable> = ArrayDeque()
		val mockHandler = mock(Handler::class.java)
		`when`(mockHandler.postDelayed(any(Runnable::class.java), anyLong())).thenAnswer { invocation ->
			val argument = invocation.getArgumentAt(0, Runnable::class.java)
			queue.add(argument)
			null
		}
		`when`(mockHandler.removeCallbacks(any(Runnable::class.java))).thenAnswer { invocation ->
			val argument = invocation.getArgumentAt(0, Runnable::class.java)
			queue.removeIf { t ->
				t == argument
			}
			null
		}
		return Pair(queue, mockHandler)
	}

	private fun createProgressBarMock(): Pair<AtomicInteger, ProgressBar> {
		val progress = AtomicInteger()
		val mockBar = mock(ProgressBar::class.java)
		`when`(mockBar.setProgress(anyInt())).thenAnswer { invocation ->
			progress.set(invocation.getArgumentAt(0, Int::class.java))
		}
		return Pair(progress, mockBar)
	}
}
