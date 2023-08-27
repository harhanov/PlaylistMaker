package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.Track

interface LocalDataSource {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getSearchHistory(): List<Track>
}