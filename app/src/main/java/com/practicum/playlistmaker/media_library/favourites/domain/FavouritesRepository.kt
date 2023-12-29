package com.practicum.playlistmaker.media_library.favourites.domain

import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {

    suspend fun addTrackToFavourites(track: TrackModel)
    suspend fun removeTrackFromFavourites(track: TrackModel)
    fun getFavouriteTracks(): Flow<List<TrackModel>>
}