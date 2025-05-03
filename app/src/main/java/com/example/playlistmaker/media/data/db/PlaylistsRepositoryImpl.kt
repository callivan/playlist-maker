package com.example.playlistmaker.media.data.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.playlistmaker.consts.Const
import com.example.playlistmaker.media.data.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.api.PlaylistsRepository
import com.example.playlistmaker.media.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PlaylistsRepositoryImpl(
    private val appDb: AppDb,
    private val playlistDbConvertor: PlaylistDbConverter,
) : PlaylistsRepository {

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDb.playlistsDao().getPlaylists()

        emit(convertFromPlaylistEntity(playlists))
    }

    override fun createPlaylist(playlist: Playlist): Flow<Unit> = flow {
        emit(appDb.playlistsDao().createPlaylist(playlistDbConvertor.map(playlist)))
    }

    override fun insertTrackIdInPlaylistIfNotExists(
        playlistId: Long,
        trackId: String
    ): Flow<Playlist?> = flow {
        val playlist = appDb.playlistsDao().insertTrackIdInPlaylistIfNotExists(playlistId, trackId)

        emit(if (playlist != null) playlistDbConvertor.map(playlist) else null)
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
}