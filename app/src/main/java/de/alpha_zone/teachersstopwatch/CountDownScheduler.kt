package de.alpha_zone.teachersstopwatch

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.time.ExperimentalTime

@ExperimentalTime
class CountDownScheduler(private val countDown: CountDown) {

	companion object {
		const val NOTIFICATION_NAME = "TimerNumber"
	}

	private val scheduled: MutableList<Intent> = mutableListOf()
	val hasScheduledTasks: Boolean get() = scheduled.isNotEmpty()

	fun schedule(context: Context): CountDownScheduler {
		val unfinished = countDown.unfinishedTimers
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
		if (alarmManager != null) {
			for (requestCode in unfinished.indices) {
				val intent = Intent(Notifier.TIMEOUT_ACTION)
				val alarmIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
				val millis = LocalDateTime.now().until(unfinished[requestCode], ChronoUnit.MILLIS)
				val time = SystemClock.elapsedRealtime() + millis
				alarmManager.setExact(
						AlarmManager.ELAPSED_REALTIME,
						time,
						alarmIntent
				)
				scheduled.add(intent)
			}
		}
		return this
	}

	fun cancel(context: Context) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
		for (requestCode in scheduled.indices) {
			val intent = Intent()
			val pendingIntent = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE)
			if (pendingIntent != null && alarmManager != null) {
				alarmManager.cancel(pendingIntent)
			}
		}
		scheduled.clear()
	}

}
