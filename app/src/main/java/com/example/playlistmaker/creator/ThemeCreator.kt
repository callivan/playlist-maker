package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.theme.data.sharedPrefs.Theme
import com.example.playlistmaker.theme.data.sharedPrefs.ThemeRepositoryImpl
import com.example.playlistmaker.theme.domain.api.ThemeRepository
import com.example.playlistmaker.theme.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.theme.domain.models.ThemeInteractor

object ThemeCreator {
    private fun getThemeRepository(context: Application): ThemeRepository {
        return ThemeRepositoryImpl(Theme(context))
    }

    fun provideThemeInteractor(context: Application): ThemeInteractor {
        return ThemeInteractorImpl(getThemeRepository(context))
    }
}