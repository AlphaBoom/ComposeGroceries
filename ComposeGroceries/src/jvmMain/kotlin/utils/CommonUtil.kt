package utils

import kotlinx.coroutines.delay
import org.checkerframework.common.value.qual.IntRange

suspend fun <T> retry(fallback: T, times: @IntRange(from = 1) Int = 1, initialDelay: Long = 100, maxDelay: Long = 1000, factor: Float = 2.0f, block: suspend () -> T): T {
    var currentDelay = initialDelay
    repeat(times) {
        try {
            return block()
        } catch (e: Exception) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return fallback // last attempt
}