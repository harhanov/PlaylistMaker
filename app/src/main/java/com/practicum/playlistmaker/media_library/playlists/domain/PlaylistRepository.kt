package com.practicum.playlistmaker.media_library.playlists.domain

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun getAllNotEmptyPlaylists(): Flow<List<PlaylistModel>>
    suspend fun addPlaylist(playlist: PlaylistModel)
}