package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.ITunesAPIRequest
import com.example.playlistmaker.search.data.dto.ITunesAPIResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    override fun getTracks(text: String): Flow<TracksResponse> = flow {
        val res = networkClient.request(ITunesAPIRequest(text))

        val data =
            TracksResponse(tracks = if (res.resultCode == 200) (res as ITunesAPIResponse).results.map {
                Track(
                    country = it.country,
                    trackId = it.trackId,
                    trackName = it.trackName,
                    previewUrl = it.previewUrl,
                    artistName = it.artistName,
                    releaseDate = it.releaseDate,
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    trackTimeMillis = it.trackTimeMillis,
                    primaryGenreName = it.primaryGenreName,
                )
            } else emptyList()).apply { resultCode = res.resultCode }

        emit(data)
    }
}