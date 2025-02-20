package com.example.playlistmaker.creator

import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.TracksRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.domain.models.TracksInteractor

object TracksSearchCreator {
        private fun getRepository(): TracksRepository {
            return TracksRepositoryImpl(RetrofitNetworkClient())
        }

        fun provideTracksInteractor(): TracksInteractor {
            return TracksInteractorImpl(getRepository())
        }
}