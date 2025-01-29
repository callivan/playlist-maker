package com.example.playlistmaker.presentation.viewState

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.TracksCreator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.adapters.TracksAdapter
import com.example.playlistmaker.presentation.models.TrackUI

class TracksViewState(
    private val recyclerView: RecyclerView,
    private val onClickTrack: (track: TrackUI, remember: Boolean) -> Unit,
    private val onInit: () -> Unit,
    private val onPending: () -> Unit,
    private val onSuccess: (tracks: List<TrackUI>) -> Unit,
    private val onError: () -> Unit,
) {
    private val tracksInteractor =
        TracksCreator.provideTracksInteractor()

    private fun converter(tracks: List<Track>): List<TrackUI> {
        return tracks.map {
            TrackUI(
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
        }
    }

    private fun pending() {
        onPending()
    }

    private fun success(t: List<Track>) {
        onSuccess(converter(t))
        recyclerView.adapter = TracksAdapter(converter(t), onClickTrack)
    }

    private fun error() {
        onError()
    }

    fun getTracks(text: String) {

        if (text.isEmpty()) {
            onInit()
        } else {
            tracksInteractor.getTracks(text, ::pending, ::success, ::error)
        }
    }
}