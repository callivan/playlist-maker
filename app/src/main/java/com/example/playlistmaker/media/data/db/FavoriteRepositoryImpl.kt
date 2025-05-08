package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.media.domain.api.FavoriteRepository
import com.example.playlistmaker.media.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDb: AppDb,
    private val trackDbConvertor: TrackDbConverter,
) : FavoriteRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDb.favoriteDao().getFavoriteTracks()

        emit(convertFromFavoriteTrackEntity(tracks))
    }

    override fun existsFavoriteTrackById(id: String): Flow<Boolean> = flow {
        emit(appDb.favoriteDao().existsFavoriteTrackById(id))
    }

    override fun insertFavoriteTrack(track: Track) = flow {
        emit(appDb.favoriteDao().insertFavoriteTrack(trackDbConvertor.map(track)))
    }

    override fun deleteFavoriteTrackById(id: String): Flow<Unit> = flow {
        emit(appDb.favoriteDao().deleteFavoriteTrackById(id))
    }

    private fun convertFromFavoriteTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}