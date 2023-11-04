package com.practicum.playlistmaker.media_library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.domain.db.FavouritesInteractor
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.launch

class FavouritesTracksViewModel(private val favouritesInteractor: FavouritesInteractor) :
    ViewModel() {

    private val _favouriteTracksState = MutableLiveData<FavouriteTracksState>()
    val favouriteTracksState: LiveData<FavouriteTracksState> = _favouriteTracksState

    init {
        updateFavouritesTrack()
    }

    fun updateFavouritesTrack() {
        viewModelScope.launch {
            favouritesInteractor.getFavouriteTracks().collect { tracks ->
                if (tracks.isNotEmpty()) {
                    _favouriteTracksState.value = FavouriteTracksState.TracksLoaded(tracks)
                } else {
                    _favouriteTracksState.value = FavouriteTracksState.Empty
                }
            }
        }
    }

}

sealed class FavouriteTracksState {
    data class TracksLoaded(val tracks: List<TrackModel>) : FavouriteTracksState()
    object Empty : FavouriteTracksState()
}