package com.example.playlistmaker.theme.domain.useCases

import androidx.appcompat.app.AppCompatDelegate

open class SwitchThemeUseCase {
    fun switchTheme(darkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}