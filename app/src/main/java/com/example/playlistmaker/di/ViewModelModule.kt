package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.media.ui.viewModels.FavoriteViewModel
import com.example.playlistmaker.media.ui.viewModels.PlaylistsViewModel
import com.example.playlistmaker.player.domain.models.PlayerInteractor
import com.example.playlistmaker.player.ui.viewModels.PlayerViewModel
import com.example.playlistmaker.playlist.domain.models.PlaylistInteractor
import com.example.playlistmaker.playlist.ui.viewModels.PlaylistViewModel
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
import com.example.playlistmaker.playlist_creator.ui.viewModels.PlaylistCreatorViewModel
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.example.playlistmaker.settings.ui.viewModels.SettingsViewModel
import com.example.playlistmaker.theme.domain.models.ThemeInteractor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<PlayerViewModel> {
        PlayerViewModel(get<PlayerInteractor>())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get<TracksInteractor>(), get<TracksHistoryIntercator>())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get<ThemeInteractor>())
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel(get<FavoriteInteractor>())
    }

    viewModel<PlaylistsViewModel> {
        PlaylistsViewModel(get<PlaylistsInteractor>())
    }

    viewModel<PlaylistCreatorViewModel> {
        PlaylistCreatorViewModel(get<PlaylistCreatorInteractor>())
    }

    viewModel<PlaylistViewModel> { PlaylistViewModel(get<PlaylistInteractor>()) }
}