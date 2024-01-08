package com.practicum.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun deletePlaylistCrossRef(playlistId: Long)

    @Query("SELECT * FROM playlists_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(playlistTrackCrossRef: PlaylistTrackCrossRef)


    @Query("UPDATE playlists_table SET numberOfTracks = :newTrackCount WHERE playlistId = :playlistId")
    suspend fun updatePlaylistCount(playlistId: Long, newTrackCount: Int)

    @Transaction
    suspend fun addTrackAndUpdateCount(playlistId: Long, trackId: Int) {
        addTrackToPlaylist(PlaylistTrackCrossRef(playlistId, trackId))
        val newTrackCount = getNumberOfTracksInPlaylist(playlistId)
        updatePlaylistCount(playlistId, newTrackCount)
    }

    @Query("DELETE FROM playlist_track_cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Int)


    @Transaction
    suspend fun removeTrackAndUpdateCount(playlistId: Long, trackId: Int) {
        removeTrackFromPlaylist(playlistId, trackId)
        val newTrackCount = getNumberOfTracksInPlaylist(playlistId)
        updatePlaylistCount(playlistId, newTrackCount)
    }

    @Query("SELECT COUNT(*) FROM playlist_track_cross_ref WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun getTrackCountInPlaylist(playlistId: Long, trackId: Int): Int

    @Query("SELECT COUNT(*) FROM playlist_track_cross_ref WHERE playlistId = :playlistId")
    suspend fun getNumberOfTracksInPlaylist(playlistId: Long): Int

    @Query("SELECT * FROM playlists_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM tracks_table WHERE trackId IN (SELECT trackId FROM playlist_track_cross_ref WHERE playlistId = :playlistId)")
    suspend fun getTracksForPlaylist(playlistId: Long): List<TrackEntity>

}