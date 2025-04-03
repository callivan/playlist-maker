package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        fun <T> debounce(
            delayMillis: Long,
            coroutineScope: CoroutineScope,
            useLastParam: Boolean,
            action: (T) -> Unit
        ): (T) -> Unit {
            var debounceJob: Job? = null
            return { param: T ->
                if (useLastParam) {
                    debounceJob?.cancel()
                }
                if (debounceJob?.isCompleted != false || useLastParam) {
                    debounceJob = coroutineScope.launch {
                        delay(delayMillis)
                        action(param)
                    }
                }
            }
        }

        private val getMs by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        private val getYear by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }

        fun msToMinSec(ms: Long): String {
            return getMs.format(ms)
        }

        fun dateToYear(date: Date): String {
            return getYear.format(date)
        }
    }
}