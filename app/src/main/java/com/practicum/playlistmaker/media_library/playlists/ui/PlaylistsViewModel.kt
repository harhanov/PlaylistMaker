package com.practicum.playlistmaker.media_library.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _playlistsState = MutableLiveData<PlaylistsState>()
    val playlistsState: LiveData<PlaylistsState> = _playlistsState


    init {
        updatePlaylists()
    }

    private fun updatePlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                if (playlists.isNotEmpty()) {
                    _playlistsState.value = PlaylistsState.PlaylistsLoaded(playlists)
                } else {
                    _playlistsState.value = PlaylistsState.Empty
                }
            }
        }
    }
}

sealed class PlaylistsState {
    data class PlaylistsLoaded(val playlists: List<PlaylistModel>) : PlaylistsState()
    object Empty : PlaylistsState()
}
