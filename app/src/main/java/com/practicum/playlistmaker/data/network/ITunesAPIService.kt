package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.SearchResult
import retrofit2.Call
import retrofit2.http.*

interface iTunesAPIService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResult>
}