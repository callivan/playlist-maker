package com.example.playlistmaker.di

import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.theme.domain.api.ThemeRepository
import com.example.playlistmaker.theme.domain.impl.ThemeInteractorImpl
import com.example.playlistmaker.theme.domain.models.ThemeInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory<ThemeInteractor> {
        ThemeInteractorImpl(get<ThemeRepository>())
    }

    single<TracksHistoryIntercator> {
        TracksHistoryInteractorImpl(get<TracksHistoryRepository>())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get<TracksRepository>())
    }
}