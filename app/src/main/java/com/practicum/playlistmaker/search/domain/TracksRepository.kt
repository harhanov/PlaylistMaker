package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.utils.Resource

interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}