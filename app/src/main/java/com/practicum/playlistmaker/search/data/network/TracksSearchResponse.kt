package com.practicum.playlistmaker.search.data.network

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.search.data.Response
import com.practicum.playlistmaker.search.data.model.TrackDTO

class TracksSearchResponse(@SerializedName("results") val results: ArrayList<TrackDTO>): Response()