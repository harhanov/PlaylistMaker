package com.practicum.playlistmaker.media_library.domain.db

import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavouritesInteractor {
    fun getFavouriteTracks(): Flow<List<TrackModel>>
    suspend fun addFavourite(trackModel: TrackModel)
    suspend fun removeFavourite(trackModel: TrackModel)
}