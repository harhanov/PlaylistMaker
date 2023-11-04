package com.practicum.playlistmaker.search.data.network

import retrofit2.http.*

interface iTunesAPIService {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TracksSearchResponse
}