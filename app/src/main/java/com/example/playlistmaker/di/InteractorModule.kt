package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.api.FavoriteRepository
import com.example.playlistmaker.media.domain.api.PlaylistsRepository
import com.example.playlistmaker.media.domain.impl.FavoriteInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.models.PlayerInteractor
import com.example.playlistmaker.playlist_creator.domain.impl.PlaylistCreatorInteractorImpl
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
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
    single<ThemeInteractor> {
        ThemeInteractorImpl(get<ThemeRepository>())
    }

    single<TracksHistoryIntercator> {
        TracksHistoryInteractorImpl(get<TracksHistoryRepository>())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get<TracksRepository>())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get<FavoriteRepository>())
    }

    single<PlaylistsInteractor> { PlaylistsInteractorImpl(get<PlaylistsRepository>()) }

    single<PlayerInteractor> {
        PlayerInteractorImpl(
            get<FavoriteInteractor>(),
            get<PlaylistsInteractor>()
        )
    }

    single<PlaylistCreatorInteractor> { PlaylistCreatorInteractorImpl(get<PlaylistsInteractor>()) }
}