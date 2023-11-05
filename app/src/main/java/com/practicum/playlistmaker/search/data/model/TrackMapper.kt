package com.practicum.playlistmaker.search.data.model

fun TrackDTO.mapToTrack(isFavourite: Boolean, orderAdded: Long): Track {
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
        previewUrl = previewUrl,
        isFavourite = isFavourite,
        orderAdded = orderAdded,
    )
}
