package com.example.playlistmaker.media.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.models.PlaylistsInteractor
import com.example.playlistmaker.media.ui.models.PlaylistUI
import com.example.playlistmaker.media.ui.models.PlaylistsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {

    private val playlistsSearchScreenStateLiveData =
        MutableLiveData<PlaylistsScreenState>(PlaylistsScreenState.Empty)

    fun getPlaylistsScreenStateLiveData(): LiveData<PlaylistsScreenState> =
        playlistsSearchScreenStateLiveData

    fun getPlaylists() {
        playlistsSearchScreenStateLiveData.postValue(PlaylistsScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getPlaylists().collect { playlists ->
                if (playlists.isNotEmpty()) {
                    playlistsSearchScreenStateLiveData.postValue(
                        PlaylistsScreenState.Content(
                            playlists.map { playlist ->
                                PlaylistUI(
                                    id = playlist.id,
                                    name = playlist.name,
                                    description = playlist.description,
                                    tracksCount = playlist.tracksCount,
                                    img = playlist.img
                                )
                            })
                    )
                } else {
                    playlistsSearchScreenStateLiveData.postValue(PlaylistsScreenState.Empty)
                }
            }
        }
    }
}