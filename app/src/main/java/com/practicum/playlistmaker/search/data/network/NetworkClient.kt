package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.Response

interface NetworkClient {
    fun doSearch(request: Any): Response
}