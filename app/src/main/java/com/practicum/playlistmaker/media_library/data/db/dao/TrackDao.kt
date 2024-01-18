package com.practicum.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFavoriteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks_table WHERE is_favorite = 1")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM tracks_table WHERE is_favorite = 1")
    suspend fun getFavoriteTrackIds(): List<Int>

}