package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.search.data.sharedPrefs.TracksHistory
import com.example.playlistmaker.search.data.sharedPrefs.TracksHistoryRepositoryImpl
import com.example.playlistmaker.search.domain.api.TracksHistoryRepository
import com.example.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator

object TracksHistoryCreator {
    private fun getTracksHistoryRepository(context: Application): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(TracksHistory(context))
    }

    fun provideTracksHistoryInteractor(context: Application): TracksHistoryIntercator {
        return TracksHistoryInteractorImpl(getTracksHistoryRepository(context))
    }
}