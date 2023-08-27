package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.SERVER_ERROR
import com.practicum.playlistmaker.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.local.LocalDataSourceImpl
import com.practicum.playlistmaker.search.data.model.mapToTrack
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.TracksSearchResponse
import com.practicum.playlistmaker.search.domain.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.utils.Resource


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalDataSourceImpl
) :
    TracksRepository {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doSearch(
            TracksSearchRequest(
                query
            )
        )

        return when (val tracksSearchResponse = response as? TracksSearchResponse) {
            null -> Resource.Error(message = SERVER_ERROR, code = response.resultCode)
            else -> Resource.Success(
                tracksSearchResponse.results.map { it.mapToTrack() },
                code = SUCCESS_CODE
            )
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return localStorage.getSearchHistory()
    }
}
