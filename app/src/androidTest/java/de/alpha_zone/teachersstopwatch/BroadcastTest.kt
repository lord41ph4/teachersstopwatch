package de.alpha_zone.teachersstopwatch

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.time.ExperimentalTime


@ExperimentalTime
@RunWith(AndroidJUnit4::class)
class BroadcastTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    fun getAlarm() :AlarmManager {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(appContext, CountDown::class.java)
        val alarmIntent = PendingIntent.getBroadcast(appContext, 0, intent, 0)
        val systemService = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return systemService
    }
}