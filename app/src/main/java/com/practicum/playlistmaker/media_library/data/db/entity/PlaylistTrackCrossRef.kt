package com.practicum.playlistmaker.media_library.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlist_track_cross_ref", primaryKeys = ["playlistId", "trackId"])
data class PlaylistTrackCrossRef(
    val playlistId: Int,
    val trackId: Int
)