package com.practicum.playlistmaker.media_library.playlists.domain.impl

import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor  {
    override suspend fun getPlaylists(): Flow<List<PlaylistModel>> {
        return playlistRepository.getAllNotEmptyPlaylists()
    }

    override suspend fun addPlaylist(playlist: PlaylistModel): Boolean {
        playlistRepository.addPlaylist(playlist)
        return true
    }
}