package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ITunesAPIRequest
import com.example.playlistmaker.data.dto.ITunesAPIResponse
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksResponse

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    override fun getTracks(text: String): TracksResponse {
        val res = networkClient.request(ITunesAPIRequest(text))

        return TracksResponse(tracks = if (res.resultCode == 200) (res as ITunesAPIResponse).results.map {
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
        } else emptyList()).apply { res.resultCode }
    }
}