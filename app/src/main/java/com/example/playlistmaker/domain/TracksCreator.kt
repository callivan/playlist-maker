package com.example.playlistmaker.domain

import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.models.TracksInteractor

object TracksCreator {
    private fun getTracksRepository(
    ): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(
    ): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }
}