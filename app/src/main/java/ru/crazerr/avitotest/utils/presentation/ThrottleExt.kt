package ru.crazerr.avitotest.utils.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun throttleLatest(
    intervalMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: () -> Unit,
): () -> Unit {
    var throttleJob: Job? = null
    return {
        if (throttleJob?.isActive != true) {
            throttleJob = coroutineScope.launch {
                delay(intervalMs)
                destinationFunction()
            }
        }
    }
}
