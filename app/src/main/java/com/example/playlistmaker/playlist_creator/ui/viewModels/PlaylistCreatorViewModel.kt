package com.example.playlistmaker.playlist_creator.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist_creator.domain.models.Playlist
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistCreatorScreenState
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistUI
import com.example.playlistmaker.utils.CustomTextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class PlaylistCreatorViewModel(private val playlistCreatorInteractor: PlaylistCreatorInteractor) :
    ViewModel() {

    val playlist = PlaylistUI()

    private val playlistCreatorLiveData = MutableLiveData<PlaylistCreatorScreenState>(
        PlaylistCreatorScreenState.Init(playlist)
    )

    fun getPlaylistCreatorLiveData(): LiveData<PlaylistCreatorScreenState> = playlistCreatorLiveData

    fun nameTextWatcher() = CustomTextWatcher.customTextWatcher { s ->
        playlist.name = s.toString().trim()

        playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.ChangeData(playlist))
    }

    fun descriptionTextWatcher() = CustomTextWatcher.customTextWatcher { s ->
        playlist.description = s.toString().trim()

        playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.ChangeData(playlist))
    }

    fun setImage(img: Uri) {
        playlist.img = img.toString()

        playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.ChangeData(playlist))
    }

    fun setTrackId(trackId: String) {
        playlist.tracksId.add(trackId)
    }

    fun createPlaylist(inputStream: InputStream?, filesDir: File?) {
        if (playlist == null) return

        viewModelScope.launch(Dispatchers.IO) {
            if (inputStream !== null && filesDir !== null) {
                playlistCreatorInteractor.savePlaylistImageInStorage(inputStream, filesDir)
                    .collect { f -> playlist.img = f.path }
            }

            playlistCreatorInteractor.createPlaylist(
                Playlist(
                    id = playlist.id,
                    name = playlist.name,
                    description = playlist.description,
                    img = playlist.img,
                    tracksId = playlist.tracksId
                )
            ).collect {
                playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.Created(playlist))
            }
        }
    }
}