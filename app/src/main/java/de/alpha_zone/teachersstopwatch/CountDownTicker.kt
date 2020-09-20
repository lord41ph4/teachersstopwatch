package de.alpha_zone.teachersstopwatch

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.ProgressBar
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@ExperimentalTime
class CountDownTicker(private val handler: Handler, val countDown: CountDown, private val context: Context? = null) : Runnable {

	private val delay = 1.toDuration(TimeUnit.SECONDS).toLongMilliseconds()
	private var tickBar: ProgressBar? = null
	private var repeatBar: ProgressBar? = null

	fun start(): CountDownTicker {
		countDown.start()
		run()
		return this
	}

	fun cancel() {
		handler.removeCallbacks(this)
	}

	fun withTickBar(tickBar: ProgressBar): CountDownTicker {
		this.tickBar = tickBar
		return this
	}

	fun withRepeatBar(repeatBar: ProgressBar): CountDownTicker {
		this.repeatBar = repeatBar
		return this
	}

	override fun run() {
		countDown.sync()
		repeatBar?.progress = countDown.cyclesDone.toInt()
		tickBar?.progress = countDown.cycleDone.toInt()
		val cycle = countDown.cycle
		if (countDown.notFinished) {
			handler.postDelayed(this, delay)
		}
		if (countDown.onTimer) {
			val intent = Intent(Notifier.TIMEOUT_ACTION)
			intent.putExtra(CountDownScheduler.NOTIFICATION_NAME, cycle)
			context?.sendBroadcast(intent)
		}
	}


}
