package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.domain.models.Track

data class SearchResult(
    val resultCount: Int,
    val results: List<Track>
): Response()
