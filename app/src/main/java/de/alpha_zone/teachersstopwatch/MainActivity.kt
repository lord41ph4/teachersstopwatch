package de.alpha_zone.teachersstopwatch

import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.NumberPicker
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


@ExperimentalTime
class MainActivity : AppCompatActivity() {

	private val handler = Handler(Looper.getMainLooper())
	private var ticker: CountDownTicker? = null
	private var scheduler: CountDownScheduler? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		val notifiier = (application as TeachersStopWatch).notifier
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val secondsPicker: NumberPicker = findViewById(R.id.secondsInput)
		secondsPicker.minValue = 0
		secondsPicker.maxValue = 59

		val minutesPicker: NumberPicker = findViewById(R.id.minutesInput)
		minutesPicker.minValue = 0
		minutesPicker.maxValue = 99

		val repeatsPicker: NumberPicker = findViewById(R.id.repeatInput)
		repeatsPicker.minValue = 1
		repeatsPicker.maxValue = 99

		val alarmOnImage = this.getDrawable(R.drawable.ic_alarm)
		val button: Button = findViewById(R.id.start_button)
		alarmOnImage?.setBounds(0, 0, 210.toDP(), 210.toDP())
		val alarmOffImage = this.getDrawable(R.drawable.ic_alarm_off)
		alarmOffImage?.setBounds(0, 0, 210.toDP(), 210.toDP())
		button.setCompoundDrawables(null, alarmOnImage, null, null)

		val repeatBar: ProgressBar = findViewById(R.id.repeat)
		val tickBar: ProgressBar = findViewById(R.id.countdown)



		button.setOnClickListener {
			val actualTicker = this.ticker
			if (actualTicker != null) {
				notifiier.clear(this)
				actualTicker.cancel()
				ticker = null
				scheduler?.cancel(this)
				scheduler = null

				minutesPicker.isEnabled = true
				secondsPicker.isEnabled = true
				repeatsPicker.isEnabled = true

				repeatBar.progress = 0
				tickBar.progress = 0
				button.setCompoundDrawables(null, alarmOnImage, null, null)
			} else {
				val ofMinutes = minutesPicker.value.toDuration(DurationUnit.MINUTES)
				val ofSeconds = secondsPicker.value.toDuration(DurationUnit.SECONDS)
				val duration = ofMinutes.plus(ofSeconds)
				if (duration.inSeconds > 0) {
					minutesPicker.isEnabled = false
					secondsPicker.isEnabled = false
					repeatsPicker.isEnabled = false
					button.setCompoundDrawables(null, alarmOffImage, null, null)

					val countDown = CountDown(duration, repeatsPicker.value)
					this.ticker =
							CountDownTicker(handler, countDown).withTickBar(tickBar).withRepeatBar(repeatBar).start()
					this.scheduler = CountDownScheduler(countDown).schedule(this)
				} else {
					notifiier.notify(this, 0)
				}
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		ticker?.cancel()
	}

	private fun Int.toDP(): Int {
		return (this * Resources.getSystem().displayMetrics.density).toInt()
	}
}