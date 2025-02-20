package com.example.playlistmaker.theme.data

interface Theme {
    fun switch(isDarkTheme: Boolean): Unit
    fun get(): Boolean
}