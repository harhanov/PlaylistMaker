package com.practicum.playlistmaker.media_library.playlists.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getPlaylists(): Flow<List<PlaylistModel>>
    suspend fun addPlaylist(playlist: PlaylistModel): Boolean
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Int)
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean
    suspend fun getPlaylistById(playlistId: Long): PlaylistModel
    suspend fun calculateTotalPlayingTime(playlistId: Long): String
}
