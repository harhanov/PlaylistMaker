package com.practicum.playlistmaker.media_library.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.launch


class PlaylistInformationViewModel(
    private val playlistId: Long,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlist = MutableLiveData<PlaylistModel>()
    val playlist: LiveData<PlaylistModel> = _playlist

    private val _totalPlayingTime = MutableLiveData<String>()
    val totalPlayingTime: LiveData<String> = _totalPlayingTime

    private val _playlistTracks = MutableLiveData<List<TrackModel>?>()
    val playlistTracks: MutableLiveData<List<TrackModel>?> = _playlistTracks

    init {
        viewModelScope.launch {
            try {
                val playlist = playlistInteractor.getPlaylistById(playlistId)
                _playlist.value = playlist

                calculateTotalPlayingTime()
                loadPlaylistTracks()
            } catch (_: Exception) {

            }
        }
    }

    private fun loadPlaylistTracks() {
        viewModelScope.launch {
            try {
                val tracks = playlistInteractor.getTracksForPlaylist(playlistId)
                _playlistTracks.value = tracks
            } catch (_: Exception) {
            }
        }
    }

    fun calculateTotalPlayingTime() {
        viewModelScope.launch {
            try {
                val totalPlayingTime = playlistInteractor.calculateTotalPlayingTime(playlistId)
                _totalPlayingTime.value = totalPlayingTime
            } catch (_: Exception) {

            }
        }
    }

}

