package com.example.playlistmaker.theme.data.sharedPrefs

import android.app.Application.MODE_PRIVATE
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.theme.data.Theme

class Theme(private val context: Context) : Theme {
    val sharedPrefs = context.getSharedPreferences(Const.THEME_PREFS, MODE_PRIVATE)

    init {
        switch(get())
    }

    override fun get(): Boolean {
        return sharedPrefs.getBoolean(Const.THEME, false)
    }

    override fun switch(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        sharedPrefs.edit().putBoolean(Const.THEME, isDarkTheme).apply()
    }
}