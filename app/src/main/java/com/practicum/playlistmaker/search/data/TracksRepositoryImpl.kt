package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.search.data.network.INTERNET_CONNECTION_ERROR
import com.practicum.playlistmaker.search.data.network.NO_INTERNET_CONNECTION_CODE
import com.practicum.playlistmaker.search.data.network.SERVER_ERROR
import com.practicum.playlistmaker.search.data.network.SUCCESS_CODE
import com.practicum.playlistmaker.search.data.model.mapToTrack
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.TracksSearchResponse
import com.practicum.playlistmaker.search.data.model.Track
import com.practicum.playlistmaker.search.domain.TracksRepository
import com.practicum.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalDataSource,
    private val playlistsDatabase: PlaylistsDatabase,
) :
    TracksRepository {
    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doSearch(
            TracksSearchRequest(query)
        )

        when (response.resultCode) {
            NO_INTERNET_CONNECTION_CODE -> {
                emit(Resource.Error(
                    message = INTERNET_CONNECTION_ERROR,
                    code = NO_INTERNET_CONNECTION_CODE
                ))
            }

            SUCCESS_CODE -> {
                with(response as TracksSearchResponse) {
                    val favouritesIDs = playlistsDatabase.getTrackDao().getFavoriteTrackIds()
                    val data = results.map() {trackResponse ->
                        val isFavourite = favouritesIDs.contains(trackResponse.trackId)
                        val orderAdded = 0L
                        trackResponse.mapToTrack(isFavourite, orderAdded)
                    }
                    emit(Resource.Success(data, code = SUCCESS_CODE))
                }
            }
            else -> {
                emit(Resource.Error(message = SERVER_ERROR, code = response.resultCode))
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override suspend fun getHistory(): List<Track> {
        val historyTracks = localStorage.getSearchHistory()
        val favouritesIDs = playlistsDatabase.getTrackDao().getFavoriteTrackIds()

        historyTracks.forEach { track ->
            track.isFavourite = favouritesIDs.contains(track.trackId)
        }
        return historyTracks
    }
}
