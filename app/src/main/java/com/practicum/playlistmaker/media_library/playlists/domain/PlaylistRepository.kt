package com.practicum.playlistmaker.media_library.playlists.domain

import com.practicum.playlistmaker.player.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getAllPlaylists(): Flow<List<PlaylistModel>>
    suspend fun addPlaylist(playlist: PlaylistModel)
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Int)
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean
    suspend fun getPlaylistById(playlistId: Long): PlaylistModel
    suspend fun getTotalPlayingTime(playlistId: Long): String
    suspend fun getTracksForPlaylist(playlistId: Long): List<TrackModel>
}