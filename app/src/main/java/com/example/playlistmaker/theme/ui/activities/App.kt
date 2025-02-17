package com.example.playlistmaker.theme.ui.activities

import android.app.Application
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.settings.domain.useCases.SwitchThemeUseCase

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(Const.THEME_PREFS, MODE_PRIVATE)

        SwitchThemeUseCase().switchTheme(sharedPrefs.getBoolean(Const.THEME, false))
    }
}