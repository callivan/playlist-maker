package com.example.playlistmaker.media.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.media.data.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.converters.PlaylistTrackDbConverter
import com.example.playlistmaker.media.data.converters.PlaylistWithTracksDbConverter
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.media.domain.api.PlaylistsRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.domain.models.PlaylistWithTracks
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PlaylistsRepositoryImpl(
    private val appDb: AppDb,
    private val playlistDbConvertor: PlaylistDbConverter,
    private val playlistTrackDbConverter: PlaylistTrackDbConverter,
    private val playlistWithTracksDbConverter: PlaylistWithTracksDbConverter
) : PlaylistsRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDb.playlistsDao().getPlaylists()

        emit(convertFromPlaylistEntity(playlists))
    }

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> = flow {
        val data = appDb.playlistsDao().getPlaylistWithTracks(playlistId)

        emit(playlistWithTracksDbConverter.map(data))
    }

    override fun insertPlaylist(playlist: Playlist): Flow<Playlist> = flow {
        val playlist = appDb.playlistsDao().insertPlaylist(playlistDbConvertor.map(playlist))

        emit(playlistDbConvertor.map(playlist))
    }

    override fun insertPlaylistTrack(playlistId: Long, track: Track): Flow<Playlist?> = flow {
        val playlist = appDb.playlistsDao()
            .insertPlaylistTrack(playlistId, playlistTrackDbConverter.map(track))

        emit(if (playlist != null) playlistDbConvertor.map(playlist) else null)
    }

    override fun deletePlaylist(playlistId: Long) {
        appDb.playlistsDao().deletePlaylist(playlistId)
    }

    override fun deletePlaylistTrack(playlistId: Long, trackId: String) {
        appDb.playlistsDao().deletePlaylistTrack(playlistId, trackId)
    }

    override fun savePlaylistImageInStorage(
        inputStream: InputStream?,
        filesDir: File?
    ): Flow<File> = flow {
        val filePath =
            File(
                filesDir,
                Const.PLAYLIST_MAKER_IMAGES_STORAGE
            )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, System.currentTimeMillis().toString())
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.WEBP, 70, outputStream)

        emit(file)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylistTrackEntity(tracks: List<PlaylistTrackEntity>): List<Track> {
        return tracks.map { track -> playlistTrackDbConverter.map(track) }
    }
}