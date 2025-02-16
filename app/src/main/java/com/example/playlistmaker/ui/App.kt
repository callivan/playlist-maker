package com.example.playlistmaker.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.ui.activitys.THEME_PREFERENCES
import com.example.playlistmaker.ui.activitys.TYPE

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE)

        switchTheme(sharedPrefs.getBoolean(TYPE, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}