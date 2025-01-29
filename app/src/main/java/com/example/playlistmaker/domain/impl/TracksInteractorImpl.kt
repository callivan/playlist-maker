package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TracksInteractor

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun getTracks(
        text: String,
        onPending: () -> Unit,
        onSuccess: (tracks: List<Track>) -> Unit,
        onError: () -> Unit
    ) {

        val thread = Thread {
            onPending()

            val res = repository.getTracks(text)
            val tracks = res.tracks

            if (res.resultCode == 200 || res.resultCode < 400) {
                onSuccess(tracks)
            } else {
                onError()
            }
        }

        thread.start()
    }
}