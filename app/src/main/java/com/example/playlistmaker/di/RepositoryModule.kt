package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.History
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.data.sharedPrefs.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.theme.data.Theme
import com.example.playlistmaker.theme.data.sharedPrefs.ThemeRepositoryImpl
import com.example.playlistmaker.theme.domain.api.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<ThemeRepository> {
        ThemeRepositoryImpl(get<Theme>())
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(get<History>())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get<NetworkClient>())
    }
}