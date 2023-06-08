package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.Response
import com.practicum.playlistmaker.data.dto.TracksSearchRequest

interface NetworkClient {
    fun doSearch(request: TracksSearchRequest): Response
}