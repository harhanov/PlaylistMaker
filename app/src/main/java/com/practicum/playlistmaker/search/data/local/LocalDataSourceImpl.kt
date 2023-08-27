package com.practicum.playlistmaker.search.data.local

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.LocalDataSource
import com.practicum.playlistmaker.search.domain.Track

class LocalDataSourceImpl(private val sharedPreferences: SharedPreferences): LocalDataSource {

    private val gson: Gson = Gson()

    override fun addToHistory(track: Track) {
        val trackId = track.trackId

        val searchHistoryString = sharedPreferences.getString("search_history", null)
        val searchHistory = if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }

        searchHistory.removeAll { it.trackId == trackId }

        searchHistory.add(0, track)

        if (searchHistory.size > 10) {
            searchHistory.removeLast()
        }

        val searchHistoryJson = gson.toJson(searchHistory)
        sharedPreferences.edit().putString("search_history", searchHistoryJson).apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }

    override fun getSearchHistory(): List<Track> {
        val searchHistoryString = sharedPreferences.getString("search_history", null)
        return if (searchHistoryString != null) {
            gson.fromJson(searchHistoryString, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

}