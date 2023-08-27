package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.search.data.TracksSearchRequest

interface NetworkClient {
    fun doSearch(request: TracksSearchRequest): Response
}