package com.example.playlistmaker.theme.data.sharedPrefs

import com.example.playlistmaker.theme.data.Theme
import com.example.playlistmaker.theme.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val theme: Theme) : ThemeRepository {
    override fun get(): Boolean {
        return theme.get()
    }

    override fun switch(isDarkTheme: Boolean) {
        theme.switch(isDarkTheme)
    }
}