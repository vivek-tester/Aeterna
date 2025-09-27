package com.aeterna.aeterna.player

import kotlinx.coroutines.*

/**
 * Lightweight sleep timer utility for the player.
 * - startMinutes(minutes, onFinish) schedules a callback after the given minutes.
 * - startMillis(millis, onFinish) schedules a callback after millis.
 * - stop() cancels the timer.
 * This is intentionally minimal; adapt it to send intents to your PlayerService or ViewModel.
 */
class SleepTimer(private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {
    private var job: Job? = null
    private var endTime: Long? = null

    fun startMinutes(minutes: Long, onFinish: () -> Unit) {
        startMillis(minutes * 60_000L, onFinish)
    }

    fun startMillis(millis: Long, onFinish: () -> Unit) {
        stop()
        endTime = System.currentTimeMillis() + millis
        job = scope.launch {
            delay(millis)
            try {
                onFinish()
            } finally {
                stop()
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        endTime = null
    }

    fun isRunning(): Boolean = job?.isActive == true

    fun remainingMillis(): Long? = endTime?.let { (it - System.currentTimeMillis()).coerceAtLeast(0L) }
}
