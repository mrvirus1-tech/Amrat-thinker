package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun quizCriteriaLimits_areClampedProperly() {
    val minCount = 5.coerceIn(20, 1024)
    val maxCount = 2000.coerceIn(20, 1024)
    val validCount = 100.coerceIn(20, 1024)

    assertEquals(20, minCount)
    assertEquals(1024, maxCount)
    assertEquals(100, validCount)

    val minDuration = 5.coerceIn(10, 120)
    val maxDuration = 180.coerceIn(10, 120)
    val validDuration = 45.coerceIn(10, 120)

    assertEquals(10, minDuration)
    assertEquals(120, maxDuration)
    assertEquals(45, validDuration)
  }
}
