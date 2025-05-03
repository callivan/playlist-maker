package com.example.playlistmaker.search.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.TracksHistoryIntercator
import com.example.playlistmaker.search.domain.models.TracksInteractor
import com.example.playlistmaker.search.ui.models.TrackUI
import com.example.playlistmaker.search.ui.models.TracksSearchScreenState
import com.example.playlistmaker.utils.CustomTextWatcher
import com.example.playlistmaker.utils.Utils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksSearchInteractor: TracksInteractor,
    private val tracksHistoryInteractor: TracksHistoryIntercator
) : ViewModel() {

    private val inputTextLiveData = MutableLiveData<String>("")

    private val onGetTracks =
        Utils.debounce<String>(1000L, viewModelScope, true) { text -> getTracks(text) }

    private val tracksSearchScreenStateLiveData =
        MutableLiveData<TracksSearchScreenState>(TracksSearchScreenState.Init)

    fun getInputTextLiveData(): LiveData<String> = inputTextLiveData

    fun getTracksSearchScreenStateLiveData(): LiveData<TracksSearchScreenState> =
        tracksSearchScreenStateLiveData

    fun getTracks(text: String? = null) {
        tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Pending)

        viewModelScope.launch {
            tracksSearchInteractor.getTracks(
                text = (text ?: getInputTextLiveData()).toString()
            ).catch {
                tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Error)
            }.collect { res ->

                if (res.resultCode >= 200 && res.resultCode < 400) {
                    tracksSearchScreenStateLiveData.postValue(
                        if (res.tracks.isNotEmpty())
                            TracksSearchScreenState.Content(
                                res.tracks.map {
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
                                }) else TracksSearchScreenState.Empty
                    )
                } else {
                    tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Error)
                }
            }
        }
    }

    fun textWatcher() = CustomTextWatcher.customTextWatcher { s ->
        inputTextLiveData.postValue(s.toString())

        if (s.toString().isEmpty()) {
            tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Init)
        } else {
            onGetTracks(s.toString())
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

    fun cleanTracksSearchScreenState() {
        tracksSearchScreenStateLiveData.postValue(TracksSearchScreenState.Init)
    }
}