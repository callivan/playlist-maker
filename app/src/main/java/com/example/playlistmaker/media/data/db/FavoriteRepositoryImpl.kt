package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.api.FavoriteRepository
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDb: AppDb,
    private val trackDbConvertor: TrackDbConverter,
) : FavoriteRepository {

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDb.favoriteDao().getTracks()

        emit(convertFromTrackEntity(tracks))
    }

    override fun existsById(id: String): Flow<Boolean> = flow {
        emit(appDb.favoriteDao().existsById(id))
    }

    override fun insertTrack(track: TrackEntity) = flow {
        emit(appDb.favoriteDao().insertTrack(track))
    }

    override fun deleteTrackById(id: String): Flow<Unit> = flow {
        emit(appDb.favoriteDao().deleteTrackById(id))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}