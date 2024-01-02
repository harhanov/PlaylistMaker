package com.practicum.playlistmaker.media_library.playlists.data

import com.practicum.playlistmaker.media_library.data.converters.PlaylistDBConverter
import com.practicum.playlistmaker.media_library.data.converters.TrackDBConverter
import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.utils.PlaylistUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val playlistConverter: PlaylistDBConverter,
    private val trackConverter: TrackDBConverter,
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

    override suspend fun getPlaylistById(playlistId: Long): PlaylistModel {
        val playlistEntity = playlistDao.getPlaylistById(playlistId)
            ?: throw NoSuchElementException("Playlist not found")

        return playlistConverter.mapToModel(playlistEntity)
    }

    override suspend fun getTotalPlayingTime(playlistId: Long): String {
        val tracks = playlistDao.getTracksForPlaylist(playlistId)
        val totalPlayingTimeMinutes = tracks.sumOf { convertTimeStringToMinutes(it.trackTime) }
        return "$totalPlayingTimeMinutes ${PlaylistUtils.getMinutesWord(totalPlayingTimeMinutes)}"
    }

    override suspend fun getTracksForPlaylist(playlistId: Long): List<TrackModel> {
        return withContext(Dispatchers.IO) {
            val tracks = playlistDao.getTracksForPlaylist(playlistId)
            tracks.map { trackConverter.mapToModel(it) }
        }
    }
    private fun convertTimeStringToMinutes(timeString: String?): Long {
        return (timeString?.toLongOrNull() ?: 0) / 60000
    }
}
