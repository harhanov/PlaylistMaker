package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.Response

interface NetworkClient {
    suspend fun doSearch(request: Any): Response
}