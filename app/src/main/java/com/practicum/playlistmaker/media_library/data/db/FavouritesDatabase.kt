package com.practicum.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.media_library.data.db.dao.TrackDao
import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao

}