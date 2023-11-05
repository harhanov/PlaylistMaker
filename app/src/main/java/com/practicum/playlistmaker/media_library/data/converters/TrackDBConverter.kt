package com.practicum.playlistmaker.media_library.data.converters

import com.practicum.playlistmaker.media_library.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.domain.TrackModel

class TrackDBConverter {
    fun map(track: TrackModel): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavourite,
            track.orderAdded
        )
    }

    fun map(track: TrackEntity): TrackModel {
        return TrackModel(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavourite,
            track.orderAdded,
        )
    }
}
