package de.alpha_zone.teachersstopwatch

import org.junit.runner.RunWith
import org.junit.runners.Suite
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RunWith(Suite::class)
@Suite.SuiteClasses(
		CountDownTest::class,
		CountDownTickerTest::class,
		CountDownSchedulerTest::class
)
class BackendTests
