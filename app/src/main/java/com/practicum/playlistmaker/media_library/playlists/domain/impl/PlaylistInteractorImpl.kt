package com.practicum.playlistmaker.media_library.playlists.domain.impl

import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {
    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addPlaylist(playlist: PlaylistModel): Boolean {
        playlistRepository.addPlaylist(playlist)
        return true
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: Int) {
        withContext(Dispatchers.IO) {
            playlistRepository.addTrackToPlaylist(playlistId, trackId)
        }
    }

    override suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            playlistRepository.isTrackInPlaylist(playlistId, trackId)
        }
    }

    override suspend fun getPlaylistById(playlistId: Long): PlaylistModel {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override suspend fun calculateTotalPlayingTime(playlistId: Long): String {
        return playlistRepository.getTotalPlayingTime(playlistId)
    }

    override suspend fun getTracksForPlaylist(playlistId: Long): List<TrackModel> {
        return playlistRepository.getTracksForPlaylist(playlistId)
    }
}