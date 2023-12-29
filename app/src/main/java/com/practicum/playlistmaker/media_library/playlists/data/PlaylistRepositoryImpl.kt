package com.practicum.playlistmaker.media_library.playlists.data

import com.practicum.playlistmaker.media_library.data.converters.PlaylistDBConverter
import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val playlistConverter: PlaylistDBConverter,
    private val playlistDao: PlaylistDao,
) : PlaylistRepository {

    override suspend fun getAllPlaylists(): Flow<List<PlaylistModel>> {
        return playlistsDatabase.getPlaylistDao().getPlaylists().map { it ->
            it.map { playlistConverter.mapToModel(it) }
        }
    }

    override suspend fun addPlaylist(playlist: PlaylistModel) {
        playlistsDatabase.getPlaylistDao().insertPlaylist(playlistConverter.mapToEntity(playlist))
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: Int) {
        playlistsDatabase.getPlaylistDao().addTrackAndUpdateCount(playlistId, trackId)
    }

    override suspend fun isTrackInPlaylist(playlistId: Long, trackId: Int): Boolean {
        val isInPlaylist = withContext(Dispatchers.IO) {
            playlistDao.getTrackCountInPlaylist(playlistId, trackId) > 0
        }
        return isInPlaylist
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<PlaylistModel> {
        return playlists.map { playlist -> playlistConverter.mapToModel(playlist) }
    }
}
