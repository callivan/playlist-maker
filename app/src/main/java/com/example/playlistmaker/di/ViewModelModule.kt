package com.example.playlistmaker.di

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
        TrackViewModel()
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get<TracksInteractor>(), get<TracksHistoryIntercator>())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get<ThemeInteractor>())
    }
}