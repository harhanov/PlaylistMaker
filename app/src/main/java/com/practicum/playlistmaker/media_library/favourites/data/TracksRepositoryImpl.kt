package com.practicum.playlistmaker.media_library.favourites.data

import com.practicum.playlistmaker.media_library.data.converters.TrackDBConverter
import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_library.favourites.domain.TracksRepository
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val trackDBConverter: TrackDBConverter,
) : TracksRepository {
    override suspend fun addTrackToBase(track: TrackModel, isFavourite: Boolean) {
        val currentTime = System.currentTimeMillis()
        val updatedTrack = track.copy(orderAdded = currentTime, isFavourite = isFavourite)
        val trackEntity = trackDBConverter.mapToEntity(updatedTrack)
        playlistsDatabase.getTrackDao().insertFavoriteTrack(trackEntity)
    }

    override suspend fun removeTrackFromFavourites(track: TrackModel) {
        val updatedTrack = track.copy(isFavourite = false)
        val trackEntity = trackDBConverter.mapToEntity(updatedTrack)
        playlistsDatabase.getTrackDao().deleteFavoriteTrack(trackEntity)
    }

    override fun getFavouriteTracks(): Flow<List<TrackModel>> = flow{
        val tracks = playlistsDatabase.getTrackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackModel> {
        return tracks.map { track -> trackDBConverter.mapToModel(track) }
    }

}