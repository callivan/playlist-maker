package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Date {
    private val getMs by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val getYear by lazy { SimpleDateFormat("yyyy", Locale.getDefault()) }

    fun msToMinSec(ms: Long): String {
        return getMs.format(ms)
    }

    fun dateToYear(date: Date): String {
        return getYear.format(date)
    }
}