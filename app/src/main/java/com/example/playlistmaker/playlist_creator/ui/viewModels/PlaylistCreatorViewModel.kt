package com.example.playlistmaker.playlist_creator.ui.viewModels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.playlist_creator.domain.models.Playlist
import com.example.playlistmaker.playlist_creator.domain.models.PlaylistCreatorInteractor
import com.example.playlistmaker.playlist_creator.domain.models.Track
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistCreatorScreenState
import com.example.playlistmaker.playlist_creator.ui.models.PlaylistUI
import com.example.playlistmaker.playlist_creator.ui.models.TrackUI
import com.example.playlistmaker.utils.CustomTextWatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class PlaylistCreatorViewModel(private val playlistCreatorInteractor: PlaylistCreatorInteractor) :
    ViewModel() {

    var playlist = PlaylistUI()

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

    fun setPlaylistData(playlistData: PlaylistUI) {
        playlist = PlaylistUI(
            id = playlistData.id,
            name = playlistData.name,
            description = playlistData.description,
            img = playlistData.img,
            tracksCount = playlist.tracksCount
        )

        playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.ChangeData(playlist))
    }

    fun setImage(img: Uri) {
        playlist.img = img.toString()

        playlistCreatorLiveData.postValue(PlaylistCreatorScreenState.ChangeData(playlist))
    }

    fun createPlaylist(track: TrackUI?, inputStream: InputStream?, filesDir: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageFile = if (inputStream !== null && filesDir !== null)
                playlistCreatorInteractor.savePlaylistImageInStorage(inputStream, filesDir)
                    .first() else null

            val createdPlaylist = playlistCreatorInteractor.insertPlaylist(
                Playlist(
                    id = playlist.id,
                    name = playlist.name,
                    description = playlist.description,
                    img = if (imageFile != null) imageFile.path else playlist.img,
                    tracksCount = playlist.tracksCount
                )
            ).first()

            val updatedPlaylist = if (track != null) playlistCreatorInteractor.insertPlaylistTrack(
                createdPlaylist.id,
                convertFromTrack(track)
            ).first() else null

            playlistCreatorLiveData.postValue(
                PlaylistCreatorScreenState.Created(
                    convertFromPlaylist(updatedPlaylist ?: createdPlaylist)
                )
            )

        }
    }

    private fun convertFromPlaylist(playlist: Playlist): PlaylistUI {
        return PlaylistUI(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            tracksCount = playlist.tracksCount,
            img = playlist.img
        )
    }

    private fun convertFromTrack(track: TrackUI): Track {
        return Track(
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
}