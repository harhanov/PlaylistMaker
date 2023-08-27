package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.Track


data class SearchResult(
    val resultCount: Int,
    val results: List<Track>
): Response()
