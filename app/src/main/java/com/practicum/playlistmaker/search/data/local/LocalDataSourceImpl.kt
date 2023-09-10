package com.practicum.playlistmaker.search.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.LocalDataSource
import com.practicum.playlistmaker.search.data.model.Track

class LocalDataSourceImpl(private val sharedPreferences: SharedPreferences): LocalDataSource {

    private val gson: Gson = Gson()

    override fun addToHistory(track: Track) {
        val trackId = track.trackId
        val searchHistoryString = sharedPreferences.getString(HISTORY_KEY, null)
        val searchHistory = if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        searchHistory.removeAll { it.trackId == trackId }

        searchHistory.add(0, track)

        if (searchHistory.size > SIZE_OF_HISTORY) {
            searchHistory.removeLast()
        }

        val searchHistoryJson = gson.toJson(searchHistory)
        sharedPreferences.edit().putString(HISTORY_KEY, searchHistoryJson).apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(HISTORY_KEY).apply()
    }

    override fun getSearchHistory(): List<Track> {
        val searchHistoryString = sharedPreferences.getString(HISTORY_KEY, null)
        return if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    private companion object {
        const val HISTORY_KEY = "search_history"
        const val SIZE_OF_HISTORY = 10
    }

}