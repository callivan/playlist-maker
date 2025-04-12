package com.example.playlistmaker.di


import com.example.playlistmaker.media.domain.models.FavoriteInteractor
import com.example.playlistmaker.media.ui.viewModels.FavoriteViewModel
import com.example.playlistmaker.player.domain.models.MediaFavoriteInteractor
import com.example.playlistmaker.player.ui.viewModels.TrackViewModel
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.ui.viewModels.SearchViewModel
import com.example.playlistmaker.settings.ui.viewModels.SettingsViewModel
import com.example.playlistmaker.theme.domain.models.ThemeInteractor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<TrackViewModel> {
        TrackViewModel(get<MediaFavoriteInteractor>())
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
}