package com.example.playlistmaker.theme.domain.api

interface ThemeRepository {
    fun switch(isDarkTheme: Boolean): Unit
    fun get(): Boolean
}