package com.practicum.playlistmaker.media_library.favourites.domain

import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun getFavouriteTracks(): Flow<List<TrackModel>>
    suspend fun addFavourite(trackModel: TrackModel, isFavourite: Boolean)
    suspend fun removeFavourite(trackModel: TrackModel)
}