package com.example.playlistmaker.search.ui.viewModels

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.models.TracksSearchScreenState
import com.example.playlistmaker.utils.Utils

class SearchViewModel(
    private val tracksSearchInteractor: TracksInteractor,
    private val tracksHistoryInteractor: TracksHistoryIntercator
) : ViewModel() {

    private val inputTextLiveData = MutableLiveData<String>("")

    private val tracksSearchScreenStateLiveData =
        MutableLiveData<TracksSearchScreenState>(TracksSearchScreenState.Init)

    fun getInputTextLiveData(): LiveData<String> = inputTextLiveData

    fun getTracksSearchScreenStateLiveData(): LiveData<TracksSearchScreenState> =
        tracksSearchScreenStateLiveData

    private val debouncer =
        Utils.debounceWithThread({ getTracks(getInputTextLiveData().value) }, 1000L)

    fun getTracks(text: String? = null) {
        tracksSearchInteractor.getTracks(
            text = (text ?: getInputTextLiveData()).toString(),
            onPending = {
                tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Pending)
            },
            onSuccess = { tracks ->

                if (tracks.isNotEmpty()) {
                    tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Content(tracks.map {
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
                    }))
                } else {
                    tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Empty)
                }
            },
            onError = {
                tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Error)
            })
    }

    fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputTextLiveData.postValue(s.toString())

                if (s.toString().isEmpty()) {
                    debouncer.remove()
                    tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Init)
                } else {
                    debouncer.debounce()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
    }

    fun cleanInputTextLiveData() {
        inputTextLiveData.postValue("")
    }

    fun getTracksHistory() {
        tracksSearchScreenStateLiveData.postValue(
            TracksSearchScreenState.History(
                tracksHistoryInteractor.get().map {
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
                })
        )
    }

    fun addTrackInHistory(track: TrackUI) {
        tracksHistoryInteractor.add(
            Track(
                country = track.country,
                trackId = track.trackId,
                trackName = track.trackName,
                previewUrl = track.previewUrl,
                artistName = track.artistName,
                releaseDate = track.releaseDate,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                trackTimeMillis = track.trackTimeMillis,
                primaryGenreName = track.primaryGenreName,
            )
        )
    }

    fun cleanTracksHistory() {
        tracksHistoryInteractor.clean()
        tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Init)
    }
}