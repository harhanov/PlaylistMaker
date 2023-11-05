package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(query: String): Flow<Triple<List<Track>?, String?, Int>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): List<Track>?
}