package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.dto.TrackDTO

fun TrackDTO.mapToTrack(): Track {
    return Track(trackId, trackName, artistName, trackTime, artworkUrl100, collectionName, releaseDate, primaryGenreName, country, previewUrl)
}