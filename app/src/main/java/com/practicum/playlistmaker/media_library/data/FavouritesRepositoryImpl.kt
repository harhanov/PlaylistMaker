package com.practicum.playlistmaker.media_library.data

import com.practicum.playlistmaker.media_library.data.converters.TrackDBConverter
import com.practicum.playlistmaker.media_library.data.db.FavouritesDatabase
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_library.domain.FavouritesRepository
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavouritesRepositoryImpl(
    private val favouritesDatabase: FavouritesDatabase,
    private val trackDBConverter: TrackDBConverter,
) : FavouritesRepository {
    override suspend fun addTrackToFavourites(track: TrackModel) {
        val currentTime = System.currentTimeMillis()
        val updatedTrack = track.copy(orderAdded = currentTime, isFavourite = true)
        val trackEntity = trackDBConverter.map(updatedTrack)
        favouritesDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun removeTrackFromFavourites(track: TrackModel) {
        val updatedTrack = track.copy(isFavourite = false)
        val trackEntity = trackDBConverter.map(updatedTrack)
        favouritesDatabase.trackDao().deleteTrack(trackEntity)
    }

    override fun getFavouriteTracks(): Flow<List<TrackModel>> = flow{
        val tracks = favouritesDatabase.trackDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackModel> {
        return tracks.map { track -> trackDBConverter.map(track) }
    }

}