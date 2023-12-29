package com.practicum.playlistmaker.media_library.favourites.domain

import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    suspend fun addTrackToBase(track: TrackModel, isFavourite: Boolean)
    suspend fun removeTrackFromFavourites(track: TrackModel)
    fun getFavouriteTracks(): Flow<List<TrackModel>>
}