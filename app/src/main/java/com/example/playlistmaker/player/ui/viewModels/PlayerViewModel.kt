package com.example.playlistmaker.player.ui.viewModels

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.models.PlayerInteractor
import com.example.playlistmaker.player.domain.models.Track
import com.example.playlistmaker.player.ui.models.BottomSheetScreenState
import com.example.playlistmaker.player.ui.models.PlayerScreenState
import com.example.playlistmaker.player.ui.models.PlaylistUI
import com.example.playlistmaker.player.ui.models.TrackUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

const val PLAYER_PROGRESS_DELAY = 300L

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {
    private val player = MediaPlayer()

    private var timerJob: Job? = null

    private fun setProgress() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (player.isPlaying) {
                delay(PLAYER_PROGRESS_DELAY)
                playerScreenStateLiveData.postValue(PlayerScreenState.Playing(progress = player.currentPosition))
            }
        }
    }

    private val playerScreenStateLiveData =
        MutableLiveData<PlayerScreenState>(PlayerScreenState.Pending)

    fun getPlayerScreenStateLiveData(): LiveData<PlayerScreenState> = playerScreenStateLiveData

    fun play() {
        player.start()
        setProgress()
    }

    fun pause() {
        player.pause()
        timerJob?.cancel()
        playerScreenStateLiveData.postValue(PlayerScreenState.Paused)
    }

    fun release() {
        player.release()
        timerJob?.cancel()
        playerScreenStateLiveData.postValue(PlayerScreenState.Released)
    }

    fun playbackController() {
        if (getPlayerScreenStateLiveData().value is PlayerScreenState.Playing) {
            pause()
        } else {
            play()
        }
    }

    fun prepare(url: String) {
        player.setDataSource(url)
        player.prepareAsync()

        player.setOnPreparedListener {
            playerScreenStateLiveData.postValue(PlayerScreenState.Prepared)
        }

        player.setOnCompletionListener {
            playerScreenStateLiveData.postValue(PlayerScreenState.Completed)
            timerJob?.cancel()
        }
    }

    fun existsInFavoriteDb(id: String): Flow<Boolean> {
        return playerInteractor.existsFavoriteTrackById(id)
    }

    fun insertInFavoriteDb(track: TrackUI) {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.insertFavoriteTrack(
                Track(
                    id = track.id,
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
            ).collect { }
        }
    }

    fun deleteFromFavoriteDb(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.deleteFavoriteTrackById(id).collect { }
        }
    }

    private val bottomSheetSearchScreenStateLiveData =
        MutableLiveData<BottomSheetScreenState>(BottomSheetScreenState.Empty)

    fun getBottomSheetScreenScreenStateLiveData(): LiveData<BottomSheetScreenState> =
        bottomSheetSearchScreenStateLiveData

    fun getPlaylists() {
        bottomSheetSearchScreenStateLiveData.postValue(BottomSheetScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.getPlaylists().collect { playlists ->
                if (playlists.isNotEmpty()) {
                    bottomSheetSearchScreenStateLiveData.postValue(
                        BottomSheetScreenState.Content(
                            playlists.map { playlist ->
                                PlaylistUI(
                                    id = playlist.id,
                                    name = playlist.name,
                                    description = playlist.description,
                                    tracksId = playlist.tracksId,
                                    img = playlist.img
                                )
                            })
                    )
                } else {
                    bottomSheetSearchScreenStateLiveData.postValue(BottomSheetScreenState.Empty)
                }
            }
        }
    }

    fun insertTrackIdInPlaylist(playlist: PlaylistUI, trackId: String): Unit {
        viewModelScope.launch(Dispatchers.IO) {
            playerInteractor.insertTrackIdInPlaylistIfNotExists(playlist.id, trackId)
                .collect { state ->
                    val p = if (state != null) PlaylistUI(
                        id = state.id,
                        name = state.name,
                        description = state.description,
                        tracksId = state.tracksId,
                        img = state.img
                    ) else playlist

                    bottomSheetSearchScreenStateLiveData.postValue(
                        BottomSheetScreenState.Update(
                            playlist = p,
                            state = state != null
                        )
                    )
                }
        }
    }

    override fun onCleared() {
        release()
    }
}