package com.example.playlistmaker.theme.domain.impl

import com.example.playlistmaker.theme.domain.api.ThemeRepository
import com.example.playlistmaker.theme.domain.models.ThemeInteractor

class ThemeInteractorImpl(private val repository: ThemeRepository) : ThemeInteractor {
    override fun get(): Boolean {
        return repository.get()
    }

    override fun switch(isDarkTheme: Boolean) {
        repository.switch(isDarkTheme)
    }
}