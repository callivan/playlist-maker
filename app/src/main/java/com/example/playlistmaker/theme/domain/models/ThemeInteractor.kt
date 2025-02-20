package com.example.playlistmaker.theme.domain.models

interface ThemeInteractor {
    fun switch(isDarkTheme: Boolean): Unit
    fun get(): Boolean
}