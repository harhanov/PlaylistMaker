package com.practicum.playlistmaker.search.data


import com.practicum.playlistmaker.search.data.network.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.*

interface iTunesAPIService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksSearchResponse>
}