package com.practicum.playlistmaker.media_library.favourites.domain.impl

import com.practicum.playlistmaker.media_library.favourites.domain.FavouritesRepository
import com.practicum.playlistmaker.media_library.favourites.domain.FavouritesInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class FavouritesInteractorImpl (private val favouritesRepository: FavouritesRepository):
    FavouritesInteractor {
    override fun getFavouriteTracks(): Flow<List<TrackModel>> {
        return favouritesRepository.getFavouriteTracks()
    }

    override suspend fun addFavourite(trackModel: TrackModel) {
        favouritesRepository.addTrackToFavourites(trackModel)
    }

    override suspend fun removeFavourite(trackModel: TrackModel) {
        favouritesRepository.removeTrackFromFavourites(trackModel)
    }

}