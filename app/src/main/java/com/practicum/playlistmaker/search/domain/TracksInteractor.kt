package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.data.model.Track

interface TracksInteractor {
    fun searchTracks(query: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?, code: Int)
    }

    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}