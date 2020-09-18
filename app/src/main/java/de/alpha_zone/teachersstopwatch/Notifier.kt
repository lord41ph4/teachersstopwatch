package de.alpha_zone.teachersstopwatch

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.provider.Settings
import kotlin.time.ExperimentalTime


@ExperimentalTime
class Notifier(context: Context) : BroadcastReceiver() {

	companion object {

		const val MAIN_CHANNEL_ID = "MainChannel"
		const val TIMEOUT_ACTION = "de.alpha_zone.teachersstopwatch.Timeout"

	}

	private val channel: NotificationChannel =
			NotificationChannel(MAIN_CHANNEL_ID, "Timer Notifications", NotificationManager.IMPORTANCE_DEFAULT)

	init {
		channel.description = "Notifications for timers being set or ran out";
		channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE;

		channel.enableVibration(true);
		channel.vibrationPattern = longArrayOf(100, 400, 200, 500)

		val audioAttributes = AudioAttributes.Builder()
				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
				.setUsage(AudioAttributes.USAGE_NOTIFICATION)
				.build()
		channel.setSound(Settings.System.DEFAULT_ALARM_ALERT_URI, audioAttributes)

		val manager = context.getSystemService(NotificationManager::class.java) as NotificationManager
		manager.createNotificationChannel(channel);
	}

	fun notify(context: Context, cycle: Int) {
		val manager = context.getSystemService(NotificationManager::class.java) as NotificationManager
		val notification: Notification = Notification.Builder(context, MAIN_CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentText("Timer $cycle is up!")
				.setCategory(Notification.CATEGORY_MESSAGE)
				.setAutoCancel(true)
				.build()

		manager.notify(cycle, notification)
	}

	override fun onReceive(context: Context?, intent: Intent?) {
		if (context != null && intent != null && intent.action == TIMEOUT_ACTION) {
			notify(context, intent.getIntExtra(CountDownScheduler.NOTIFICATION_NAME, 0))
		}
	}

	fun clear(context: Context) {
		val manager = context.getSystemService(NotificationManager::class.java) as NotificationManager
		manager.cancelAll()
	}
}
