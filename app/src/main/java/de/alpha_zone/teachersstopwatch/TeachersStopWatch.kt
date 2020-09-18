package de.alpha_zone.teachersstopwatch;

import android.app.Application
import android.content.IntentFilter
import kotlin.time.ExperimentalTime

@ExperimentalTime
class TeachersStopWatch : Application() {

	private lateinit var _notifier: Notifier
	val notifier: Notifier get() = _notifier

	override fun onCreate() {
		super.onCreate()
		_notifier = Notifier(this)
		registerReceiver(notifier, IntentFilter(Notifier.TIMEOUT_ACTION))
	}

	override fun onTerminate() {
		super.onTerminate()
		this.unregisterReceiver(notifier)
	}

}
