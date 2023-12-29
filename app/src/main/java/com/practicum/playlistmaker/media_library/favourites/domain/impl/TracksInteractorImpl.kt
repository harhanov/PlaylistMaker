package com.practicum.playlistmaker.media_library.favourites.domain.impl

import com.practicum.playlistmaker.media_library.favourites.domain.TracksRepository
import com.practicum.playlistmaker.media_library.favourites.domain.TracksInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl (private val tracksRepository: TracksRepository):
    TracksInteractor {
    override fun getFavouriteTracks(): Flow<List<TrackModel>> {
        return tracksRepository.getFavouriteTracks()
    }

    override suspend fun addFavourite(trackModel: TrackModel, isFavourite: Boolean) {
        tracksRepository.addTrackToBase(trackModel, isFavourite)
    }

    override suspend fun removeFavourite(trackModel: TrackModel) {
        tracksRepository.removeTrackFromFavourites(trackModel)
    }

}