package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.dto.TrackDTO

fun TrackDTO.mapToTrack(): Track {
    return Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}