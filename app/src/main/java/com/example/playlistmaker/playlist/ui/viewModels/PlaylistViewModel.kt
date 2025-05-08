package com.example.playlistmaker.playlist.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist.domain.models.PlaylistInteractor
import com.example.playlistmaker.playlist.ui.models.PlaylistScreenState
import com.example.playlistmaker.playlist.ui.models.PlaylistUI
import com.example.playlistmaker.playlist.ui.models.PlaylistWithTracksUI
import com.example.playlistmaker.playlist.ui.models.TrackUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) :
    ViewModel() {

    private val playlistScreenStateLiveData =
        MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Empty)

    fun getPlaylistScreenStateLiveData(): LiveData<PlaylistScreenState> =
        playlistScreenStateLiveData

    fun getPlaylistTracks(playlistId: Long) {
        playlistScreenStateLiveData.postValue(PlaylistScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylistWithTracks(playlistId).collect { state ->
                val playlist = PlaylistUI(
                    id = state.playlist.id,
                    name = state.playlist.name,
                    tracksCount = state.playlist.tracksCount,
                    description = state.playlist.description,
                    img = state.playlist.img
                )

                val tracks = state.tracks.map { track ->
                    TrackUI(
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
                }

                playlistScreenStateLiveData.postValue(
                    PlaylistScreenState.Content(
                        PlaylistWithTracksUI(
                            playlist = playlist,
                            tracks = tracks
                        )
                    )
                )
            }
        }
    }

    fun deletePlaylistTrack(playlistId: Long, trackId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylistTrack(playlistId, trackId)
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistId)
        }
    }
}