package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(query: String): Flow<Triple<List<Track>?, String?, Int>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> Triple(result.data, null, result.code)
                is Resource.Error -> Triple(null, result.message, result.code)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override suspend fun getHistory(): List<Track> {
        return repository.getHistory()
    }
}