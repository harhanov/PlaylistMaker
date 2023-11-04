package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}