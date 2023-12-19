package com.practicum.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.media_library.data.db.dao.TrackDao
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistTrackCrossRef
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [PlaylistEntity::class, PlaylistTrackCrossRef::class, TrackEntity::class])

abstract class PlaylistsDatabase: RoomDatabase() {
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getTrackDao(): TrackDao
}