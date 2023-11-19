package com.practicum.playlistmaker.media_library.data.converters

import com.practicum.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistModel

class PlaylistDBConverter {
    fun mapToEntity(playlist: PlaylistModel): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImagePath,
            playlist.trackIds,
            playlist.numberOfTracks,
        )
    }

    fun mapToModel(playlist: PlaylistEntity): PlaylistModel {
        return PlaylistModel(
            playlist.playlistId,
            playlist.playlistName,
            playlist.playlistDescription,
            playlist.playlistImagePath,
            playlist.trackIds,
            playlist.numberOfTracks,
        )
    }
}