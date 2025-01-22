package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DebounceWithThreadData(val debounce: () -> Unit, val remove: () -> Unit)

class Utils {
    companion object {
        fun debounceWithThread(cb: () -> Unit, delay: Long = 2000L): DebounceWithThreadData {
            val handler = Handler(Looper.getMainLooper())
            val thread = Runnable { cb() }

            val remove = { handler.removeCallbacks(thread) }

            fun debounce() {
                handler.removeCallbacks(thread)
                handler.postDelayed(thread, delay)
            }

            return DebounceWithThreadData(::debounce, remove)
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