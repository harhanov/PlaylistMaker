package com.practicum.playlistmaker

import com.practicum.playlistmaker.domain.models.Track

data class SearchResult(
    val resultCount: Int,
    val results: List<Track>
)
